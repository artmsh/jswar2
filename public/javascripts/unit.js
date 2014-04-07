function Unit(data, parentEl) {
    for (var d in data) {
        this[d] = data[d];
    }

    this.type = units[this.name];

//    this.orders = [];
//    this.orders.push(new Orders.still(UnitAction.Still, Orders.still.STANDBY));


//    this.id = "unit" + (unitCounter++);
    this.canvas = $("<canvas class='unit'></canvas>");
    this.selected = false;

    this.canvas.mouseenter(this.handleMouseEnterAndMove.bind(this));
    this.canvas.mousemove(this.handleMouseEnterAndMove.bind(this));
    this.canvas.mouseleave(this.handleMouseLeave.bind(this));

    this.canvas.attr({"width" : this.type.Image.size[0], "height" : this.type.Image.size[1]});
    this.canvas.css({"z-index": this.type.DrawLevel });
    this.context = this.canvas[0].getContext('2d');

    $(parentEl).append(this.canvas);

    this.Animation = { Frame : 0, Wait: 0, ActionIndex: 0, Direction: 0, NumDirections: this.type.NumDirections,
        Action: null, MoveX : 0, MoveY : 0 };
    this.AnimationBeforeUpdate = {};
//    if (animations[this.type.Animations]) {
//        this.Animation.Action = 'Still';
//    }

    this.Wait = 0;

    if (this.type.Building && this.Animation.NumDirections == undefined) {
        this.Animation.NumDirections = 1;
        this.Animation.Direction = 0;
    } else if (this.Animation.NumDirections == undefined) {
        this.Animation.NumDirections = 8;
        this.Animation.Direction = Math.floor(Math.random() * 8);
    }

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

Unit.prototype.draw = function(currentPlayer) {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

    // todo Animation.MoveY & Animation.MoveX change could be more efficient
    this.canvas.css({"margin-top": y + this.Animation.MoveY, "margin-left": x + this.Animation.MoveX});

    this.context.clearRect(0, 0, this.getTypeImageWidth(), this.getTypeImageHeight());
    if (this.selected) {
        if (this.player == currentPlayer) {
            this.context.strokeStyle = "green";
        } else if (this.player.type == 'Neutral') {
            this.context.strokeStyle = "yellow";
        } else {
            this.context.strokeStyle = "red";
        }
        this.context.strokeRect(
            Math.floor((this.getTypeImageWidth() - this.type.BoxSize[0]) / 2),
            Math.floor((this.getTypeImageHeight() - this.type.BoxSize[1]) / 2),
            this.type.BoxSize[0], this.type.BoxSize[1]);
    }

    var image = ResourcePreloader.get(this.image);
    if (this.Animation.Direction < 5) {
        this.canvas.removeClass('rotated');
        this.context.drawImage(image, this.Animation.Direction * this.getTypeImageWidth(),
            this.Animation.Frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
        // todo coloring
    } else {
        // draw rotated
        this.canvas.addClass('rotated');
        this.context.drawImage(image, (8 - this.Animation.Direction) * this.getTypeImageWidth(),
            this.Animation.Frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
    }
};

Unit.prototype.detach = function() {
    this.canvas.detach();
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

Unit.prototype.redrawIfNeeded = function(currentPlayer, isSelected) {
    if (this.AnimationBeforeUpdate.Frame != this.Animation.Frame ||
        this.AnimationBeforeUpdate.Direction != this.Animation.Direction ||
        this.AnimationBeforeUpdate.MoveX != this.Animation.MoveX ||
        this.AnimationBeforeUpdate.MoveY != this.Animation.MoveY ||
        this.selected != isSelected) {
        this.selected = isSelected;
        this.draw(currentPlayer);
    }
};

Unit.directionsMap = [3, 4, 5, 0, 2, 0, 6, 0, 1, 0, 7];
function index(dx, dy) { return (dx + 1) + 4 * (dy + 1); }
// x1-x2 == 0, y1-y2 == 1    1 + 4 * 2
// x1-x2 == -1 y1-y2 == 1    0 + 4 * 2
// x1-x2 == -1 y1-y2 == 0    0 + 4 * 1
// x1-x2 == -1 y1-y2 ==-1    0 + 4 * 0
// x1-x2 ==  0 y1-y2 ==-1    1 + 4 * 0
// x1-x2 ==  1 y1-y2 ==-1    2 + 4 * 0
// x1-x2 ==  1 y1-y2 == 0    2 + 4 * 1
// x1-x2 ==  1 y1-y2 == 1    2 + 4 * 2

Unit.prototype.animateAction = function(action, params) {
    if (action === 'move') {
        this.Animation.Direction = Unit.directionsMap[index(this.x - params.moveX, this.y - params.moveY)];
    }

    var anims = animations[this.type.Animations];
    if (anims && anims[capitalize(action)]) {
        this.updateAnimation(anims[capitalize(action)], 8);
    }
};

Unit.prototype.updateAnimation = function(anim, scale) {
    jQuery.extend(this.AnimationBeforeUpdate, this.Animation);

    if (anim && anim != this.Animation.Action) {
        // this.Animation.Unbreakable should != true
        this.Animation.Action = anim;
        this.Animation.Wait = 0;
        this.Animation.ActionIndex = 0;
        this.Animation.MoveX = 0;
        this.Animation.MoveY = 0;
    }

    if (this.Animation.Wait > 0) {
        this.Animation.Wait--;

        if (this.Animation.Wait == 0) {
            this.Animation.ActionIndex = (this.Animation.ActionIndex + 1) % this.Animation.Action.length;
        }

        return 0;
    }

    var move = 0;
    while (this.Animation.Wait == 0) {
        var action = this.Animation.Action[this.Animation.ActionIndex].split(" ");
        if (Animation[action[0]]) {
            move = Animation[action[0]].call(Animation, this.Animation, action.slice(1), scale);
        }
        if (this.Animation.Wait == 0) {
            this.Animation.ActionIndex = (this.Animation.ActionIndex + 1) % this.Animation.Action.length;
        }
    }

    this.Animation.Wait--;
    if (this.Animation.Wait == 0) {
        this.Animation.ActionIndex = (this.Animation.ActionIndex + 1) % this.Animation.Action.length;
    }

    return move;
};

Unit.prototype.executeAction = function() {
    if (!this.Animation.Unbreakable) {

    }
    this.orders[0].execute(this);
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