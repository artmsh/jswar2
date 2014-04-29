function Unit(data, layoutManager) {
    for (var d in data) {
        this[d] = data[d];
    }

    this.type = units[this.name];

//    this.id = "unit" + (unitCounter++);
    this.selected = false;

    this.graphics = layoutManager.addLayer(this.type.Image.size[0], this.type.Image.size[1],
        { className: 'unit',
          zIndex: this.type.DrawLevel,
          mouseenter: this.handleMouseEnterAndMove.bind(this),
          mousemove: this.handleMouseEnterAndMove.bind(this),
          mouseleave: this.handleMouseLeave.bind(this),
          mousedown: this.handleMouseDown.bind(this)
        });

    if (this.action) {
        this.animation = Animation.buildFrom(this, this);
    } else {
        console.log('WARNING: action not specified');
    }

    this.onBeforeAnimation = [];

    this.initVariables(data);
}

Unit.prototype.initVariables = function(data) {
    this.vars = {};

    this.vars.Level = 1;
    this.vars.HitPoints = this.vars.MaxHitPoints = this.type.HitPoints;

    if (this.type.GivesResource) {
        this.vars.GivesResource = this.type.GivesResource;
        this.vars.ResourceLeft = data * 2500;
    }

    this.vars.PiercingDamage = this.type.PiercingDamage;
    this.vars.MaxAttackRange = this.type.MaxAttackRange;
    if (this.type.Armor == undefined) {
        this.type.Armor = 0;
    }
    this.vars.Armor = this.type.Armor;
    this.vars.SightRange = this.type.SightRange;
    this.vars.Speed = this.type.Speed;
};

Unit.prototype.getVariable = function(name, component, kind) {
    var v;
    switch (kind) {
        case 0: {
            v = this.vars[name];
            break;
        }
        case 1: {
            v = this.type.DefaultStat.vars[name];
            break;
        }
        case 2: {
            v = this.Stats.vars[name];
            break;
        }
        default: console.log("no such kind " + kind + " for user " + this.id);
    }

    if (v !== undefined) {
        if (['Value', 'Max', 'Increase'].indexOf(component) > -1) {
            return v[component];
        } else if (component == 'Diff') {
            return v.Max - v.Value;
        } else if (component == 'Percent') {
            return ~~((100 * v.Value) / v.Max);
        } else if (component == 'Name') {
            if (name == 'GiveResource') {
                return DefaultResourceNames[unit.type.GivesResource];
            } else if (name == 'CarryResource') {
                return DefaultResourceNames[unit.CurrentResource]
            } else {
                return UnitTypeVar.VariableNameLookup[name];
            }
        }
    } else {
        console.log("variable " + name + " not exists for user " + this.id + " with kind " + kind);
    }

    return null;
};

Unit.prototype.handleMouseEnterAndMove = function(event) {
    var mouseX = event.pageX - ~~$(event.target).parent().offset().left;
    var mouseY = event.pageY - ~~$(event.target).parent().offset().top;

    if (checkIsPointInsideBox(mouseX, mouseY, this.getSelectionBox())) {
        $(event.target).parent().parent().addClass('magnifyingGlass');
    } else {
        $(event.target).parent().parent().removeClass('magnifyingGlass');
    }
};

Unit.prototype.handleMouseLeave = function(event) {
    $(event.target).parent().parent().removeClass('magnifyingGlass');
};

Unit.prototype.handleMouseDown = function(event) {
    if (event.which == 3) {
        var mouseX = event.pageX - ~~$(event.target).parent().offset().left;
        var mouseY = event.pageY - ~~$(event.target).parent().offset().top;

        if (!checkIsPointInsideBox(mouseX, mouseY, this.getSelectionBox()) && checkIsPointInsideBox(mouseX, mouseY, this.getBox())) {
            return true;
        }
    }

    return false;
};

Unit.prototype.draw = function(currentPlayer) {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

    this.graphics.canvas.css({"margin-top": y + this.animation.offsetY, "margin-left": x + this.animation.offsetX});

    this.graphics.context.clearRect(0, 0, this.getTypeImageWidth(), this.getTypeImageHeight());
    if (this.selected) {
        if (this.player == currentPlayer) {
            this.graphics.context.strokeStyle = "green";
        } else if (this.player.type == 'Neutral') {
            this.graphics.context.strokeStyle = "yellow";
        } else {
            this.graphics.context.strokeStyle = "red";
        }
        this.graphics.context.strokeRect(
            Math.floor((this.getTypeImageWidth() - this.type.BoxSize[0]) / 2),
            Math.floor((this.getTypeImageHeight() - this.type.BoxSize[1]) / 2),
            this.type.BoxSize[0], this.type.BoxSize[1]);
    }

    var image = ResourcePreloader.get(this.image);
    if (this.animation.direction < 5) {
        this.graphics.canvas.removeClass('rotated');
        this.graphics.context.drawImage(image, this.animation.direction * this.getTypeImageWidth(),
            this.animation._frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
        // todo coloring
    } else {
        // draw rotated
        this.graphics.canvas.addClass('rotated');
        this.graphics.context.drawImage(image, (this.animation.numDirections - this.animation.direction) * this.getTypeImageWidth(),
            this.animation._frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
    }
};

Unit.prototype.detach = function() {
    this.graphics.canvas.detach();
};

Unit.prototype.getCenterCoords = function() {
    // in original warcraft 2 sight center of unit appears at bottom right center of unit sprite

    return {x : (this.x + this.getTypeTileWidth()) * 32  - 16, y : (this.y + this.getTypeTileHeight()) * 32 - 16}
};

Unit.prototype.getSelectionBox = function() {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2) +
        Math.floor((this.getTypeImageWidth() - this.type.BoxSize[0]) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2) +
        Math.floor((this.getTypeImageHeight() - this.type.BoxSize[1]) / 2);

    return { startX: x, startY: y, width: this.type.BoxSize[0], height: this.type.BoxSize[1] };
};

Unit.prototype.getBox = function() {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

    return { startX: x, startY: y, width: this.getTypeImageWidth(), height: this.getTypeImageHeight() };
};

Unit.prototype.applyChangeset = function(changeSet, currentPlayer) {
    if (changeSet.x) this.onBeforeAnimation.push(function() { this.x = changeSet.x; }.bind(this));
    if (changeSet.y) this.onBeforeAnimation.push(function() { this.y = changeSet.y; }.bind(this));

    if (changeSet.action) {
        this.updateAnimation(changeSet);
        this.draw(currentPlayer);
    }

};

Unit.prototype.animateAndRedraw = function(currentPlayer, isSelected) {
    var diff = this.animation.animate();

    if (diff['_frame'] || diff['direction'] || this.selected != isSelected) {
        this.selected = isSelected;
        this.draw(currentPlayer);
    }

    if (diff['offsetX'] || diff['offsetY']) {
        var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
        var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

        this.graphics.canvas.css({"margin-top": y + this.animation.offsetY, "margin-left": x + this.animation.offsetX});
    }
};

Unit.prototype.updateAnimation = function(context) {
    this.onBeforeAnimation.forEach(function(f) { f(); });
    this.onBeforeAnimation = [];
    this.animation = Animation.buildFrom(context, this);
};

Unit.prototype.getTypeImageWidth = function() {
    return this.type.Image.size[0];
};

Unit.prototype.getTypeImageHeight = function() {
    return this.type.Image.size[1];
};

Unit.prototype.getTypeTileWidth = function() {
    return this.type.TileSize[0];
};

Unit.prototype.getTypeTileHeight = function() {
    return this.type.TileSize[1];
};

var UnitAction = { Still : 0, StandGround : 1, Built : 10 };