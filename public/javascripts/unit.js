var unitCounter = 0;

function Unit(data, layoutManager, selection, unitType) {
    this.armor = data.armor;
    this.hp = data.hp;
    this.name = data.name;
    this.player = data.player;
    this.x = data.x;
    this.y = data.y;

    this.type = unitType;
    this.uiType = units[this.name];

    this.id = "unit" + (unitCounter++);
    this.selected = false;

    var image = this.uiType.Image;
    this.layout = layoutManager.createLayout(image.size[0], image.size[1], this.id, 'unit', this.uiType.DrawLevel);

    var _this = this;
    this.layout.on('click', function(x, y, event) {
        if (event.which == 1) {
            selection.selectSingleUnit(_this);
        }
    });

    this.layout.on('mousedown', function(x, y, event) {
        if (event.which == 1) {
            selection.startSelection(x, y);
        }
    });

    if (data.action) {
        this.animation = Animation.buildFrom(data, this);
    } else {
        console.log('WARNING: action not specified');
    }

    this.onAfterAnimation = [];
}

Unit.prototype.isBuilding = function() {
    return this.type.moveSpeed == 0;
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

    this.layout.canvasEl.css({"margin-top": y + this.animation.offsetY, "margin-left": x + this.animation.offsetX});

    this.layout.context.clearRect(0, 0, this.getTypeImageWidth(), this.getTypeImageHeight());
    if (this.selected) {
        if (this.player == currentPlayer) {
            this.layout.context.strokeStyle = "green";
        } else if (this.player.type == 'Neutral') {
            this.layout.context.strokeStyle = "yellow";
        } else {
            this.layout.context.strokeStyle = "red";
        }
        this.layout.context.strokeRect(
            Math.floor((this.getTypeImageWidth() - this.type.ui.boxSize[0]) / 2),
            Math.floor((this.getTypeImageHeight() - this.type.ui.boxSize[1]) / 2),
            this.type.ui.boxSize[0], this.type.ui.boxSize[1]);
    }

    var image = ResourcePreloader.get(this.image);
    if (this.animation.direction < 5) {
        this.layout.canvasEl.removeClass('rotated');
        this.layout.context.drawImage(image, this.animation.direction * this.getTypeImageWidth(),
            this.animation._frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
        // todo coloring
    } else {
        // draw rotated
        this.layout.canvasEl.addClass('rotated');
        this.layout.context.drawImage(image, (this.animation.numDirections - this.animation.direction) * this.getTypeImageWidth(),
            this.animation._frame * this.getTypeImageHeight(), this.getTypeImageWidth(), this.getTypeImageHeight(), 0, 0,
            this.getTypeImageWidth(), this.getTypeImageHeight());
    }
};

Unit.prototype.detach = function() {
    this.layout.canvasEl.detach();
};

Unit.prototype.getCenterCoords = function() {
    // in original warcraft 2 sight center of unit appears at bottom right center of unit sprite

    return {x : (this.x + this.getTypeTileWidth()) * 32  - 16, y : (this.y + this.getTypeTileHeight()) * 32 - 16}
};

Unit.prototype.getSelectionBox = function() {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2) +
        Math.floor((this.getTypeImageWidth() - this.type.ui.boxSize[0]) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2) +
        Math.floor((this.getTypeImageHeight() - this.type.ui.boxSize[1]) / 2);

    return { startX: x, startY: y, width: this.type.ui.boxSize[0], height: this.type.ui.boxSize[1] };
};

Unit.prototype.getBox = function() {
    var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
    var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

    return { startX: x, startY: y, width: this.getTypeImageWidth(), height: this.getTypeImageHeight() };
};

var changesetActions = {
    'x': function(unit, changeSet) {
        unit.x = changeSet.x;
        unit.animation.offsetX = 0;
    },
    'y': function(unit, changeSet) {
        unit.y = changeSet.y;
        unit.animation.offsetY = 0;
    },
    'action': function(unit, changeSet) {
        if (changeSet.action == 'still') {
            var oldDirection = unit.animation.direction;
            unit.animation = Animation.buildFrom(changeSet, unit);
            unit.animation.direction = oldDirection;
        } else {
            unit.animation = Animation.buildFrom(changeSet, unit);
        }
    }
};

Unit.prototype.applyChangeset = function(changeSet) {
    var self = this;
    // care: order of changesetActions is unspecified
    Object.keys(changesetActions).forEach(function(key) {
        if (changeSet[key] !== undefined) {
            if (self.animation.unbreakableModeOn) {
                self.onAfterAnimation.push(function() { changesetActions[key].apply(null, [self, changeSet]); });
            } else {
                changesetActions[key].apply(null, [self, changeSet]);
            }
        }
    });
};

Unit.prototype.animateAndRedraw = function(currentPlayer, isSelected) {
    var diff = this.animation.animate();

    if (diff['unbreakableModeOn'] && !this.animation.unbreakableModeOn) {
        this.onAfterAnimation.forEach(function(f) { f(); });
        this.draw(currentPlayer);
        this.onAfterAnimation = [];
    }

    if (diff['_frame'] || diff['direction'] || this.selected != isSelected) {
        console.info('redraw', diff);

        this.selected = isSelected;
        this.draw(currentPlayer);
    }

    if (diff['offsetX'] || diff['offsetY']) {
        console.info('offset', diff);

        var x = this.x * 32 - Math.round((this.getTypeImageWidth() - 32 * this.getTypeTileWidth()) / 2);
        var y = this.y * 32 - Math.round((this.getTypeImageHeight() - 32 * this.getTypeTileHeight()) / 2);

        this.layout.canvasEl.css({"margin-top": y + this.animation.offsetY, "margin-left": x + this.animation.offsetX});
    }
};

Unit.prototype.getTypeImageWidth = function() {
    return this.uiType.Image.size[0];
};

Unit.prototype.getTypeImageHeight = function() {
    return this.uiType.Image.size[1];
};

Unit.prototype.getTypeTileWidth = function() {
    return this.type.basic.unitSize[0];
};

Unit.prototype.getTypeTileHeight = function() {
    return this.type.basic.unitSize[1];
};