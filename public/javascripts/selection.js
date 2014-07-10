function Selection(map, selectionRectColor, selectionListener, units, layoutManager, currentPlayer) {
    this.targets = {};
    this.map = map;
    this.currentPlayer = currentPlayer;
    this.units = units;
    this.selectionRectColor = selectionRectColor;
    this.selectionListener = selectionListener;

    this.layoutManager = layoutManager;
    this.layout = layoutManager.createLayout(map.width * 32, map.height * 32, 'selection', '', -1);

    var _this = this;
    this.layout.on('mousemove', function(x, y, event) {
        if (event.which == 1) {
            _this.continueSelection(x, y);
        } else console.warn('Selection active only if left button pressed');
    });

    this.layout.on('mouseup', function(x, y, event) {
        if (event.which == 1) {
            _this.endSelection(x, y, event);
        } else console.warn('Selection active only if left button pressed');
    });

    this.lastDrawedSelectionBox = Selection.ZERO_SELECTION;

//    $(map.canvas).parent().children("canvas#map, canvas.unit").mousedown(this.handleMouseDown.bind(this));
//    $(map.canvas).parent().children("canvas#selection").mouseup(this.handleMouseUp.bind(this));
//    $(map.canvas).parent().children("canvas#selection").mousemove(this.handleMouseMove.bind(this));
}

Selection.ZERO_SELECTION = { startX : 0, startY : 0, width : 0, height : 0 };

Selection.prototype.redraw = function() {
    if (this.isSelectionActive()) {
        var selectionBox = this.getCurrentSelectionBox();

        if (!isObjectsEqual(this.lastDrawedSelectionBox, selectionBox)) {
            this.layout.context.clearRect(this.lastDrawedSelectionBox.startX - 1, this.lastDrawedSelectionBox.startY - 1,
                this.lastDrawedSelectionBox.width + 2, this.lastDrawedSelectionBox.height + 2);

            this.layout.context.strokeStyle = this.selectionRectColor;
            this.layout.context.strokeRect(selectionBox.startX, selectionBox.startY, selectionBox.width, selectionBox.height);

            this.lastDrawedSelectionBox = selectionBox;
        }
    } else if (!isObjectsEqual(this.lastDrawedSelectionBox, Selection.ZERO_SELECTION)) {
        this.layout.context.clearRect(this.lastDrawedSelectionBox.startX - 1, this.lastDrawedSelectionBox.startY - 1,
            this.lastDrawedSelectionBox.width + 2, this.lastDrawedSelectionBox.height + 2);
        this.lastDrawedSelectionBox = Selection.ZERO_SELECTION;
    }
};

Selection.prototype.isSelectionActive = function() {
    return this.layout.canvasEl.hasClass('active');
};

Selection.prototype.getCurrentSelectionBox = function() {
    return normalize(this.startX, this.startY, this.endX, this.endY);
};

Selection.prototype.startSelection = function(x, y) {
    console.info('start selection from (%d %d)', x, y);
    this.layoutManager.setCursor('crosshair');
    this.startX = x;
    this.startY = y;

    this.endX = this.startX;
    this.endY = this.startY;

    this.layout.canvasEl.addClass('active');
};

Selection.prototype.continueSelection = function(x, y) {
    if (this.isSelectionActive()) {
        this.endX = x;
        this.endY = y;
    } else console.warn('Selection should be active');
};

Selection.prototype.endSelection = function(x, y, event) {
    console.info('end selection on (%d %d)', x, y);

    this.layoutManager.setCursor('pointer');
    $(this.canvas).removeClass('active');

    if (event.shiftKey != 1) {
        this.targets = {};
    }

    var selectionBox = normalize(this.startX, this.startY, x, y);
    var matched = Object.keys(this.units).filter(function(unitId) {
        return isIntersectOrInside(selectionBox, this.units[unitId].getSelectionBox());
    }.bind(this));

    if (matched.length > 1) {
        var groupedByPlayer = groupBy(matched, function(o) { return this.units[o].player == this.currentPlayer; }, this);
        if (groupedByPlayer[true]) {
            matched = groupedByPlayer[true].filter(function(o) { return this.units[o].type.SelectableByRectangle; }, this);
        } else if (groupedByPlayer[false]) {
            matched = groupedByPlayer[false].slice(0, 1);
        }
    }
    matched.forEach(function(o) { this.targets[o] = this.units[o]; }, this);

    this.selectionListener.fireSelectionChanged();
};

Selection.prototype.selectSingleUnit = function(unit) {

};