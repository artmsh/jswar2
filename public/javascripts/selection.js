function Selection(selectionEl, map, currentPlayer, units, selectionRectColor, selectionListener) {
    this.targets = {};
    this.map = map;
    this.currentPlayer = currentPlayer;
    this.units = units;
    this.selectionRectColor = selectionRectColor;
    this.selectionListener = selectionListener;

    this.canvas = selectionEl;
    this.context = selectionEl.getContext('2d');

    this.canvas.width = map.width * 32;
    this.canvas.height = map.height * 32;

    this.lastDrawedSelectionBox = Selection.ZERO_SELECTION;

    $(map.canvas).parent().children("canvas#map, canvas.unit").mousedown(this.handleMouseDown.bind(this));
    $(map.canvas).parent().children("canvas#selection").mouseup(this.handleMouseUp.bind(this));
    $(map.canvas).parent().children("canvas#selection").mousemove(this.handleMouseMove.bind(this));
}

Selection.ZERO_SELECTION = { startX : 0, startY : 0, width : 0, height : 0};

Selection.prototype.redraw = function() {
    if (this.isSelectionActive()) {
        var selectionBox = this.getCurrentSelectionBox();

        if (!isObjectsEqual(this.lastDrawedSelectionBox, selectionBox)) {
            this.context.clearRect(this.lastDrawedSelectionBox.startX - 1, this.lastDrawedSelectionBox.startY - 1,
                this.lastDrawedSelectionBox.width + 2, this.lastDrawedSelectionBox.height + 2);

            this.context.strokeStyle = this.selectionRectColor;
            this.context.strokeRect(selectionBox.startX, selectionBox.startY, selectionBox.width, selectionBox.height);

            this.lastDrawedSelectionBox = selectionBox;
        }
    } else if (!isObjectsEqual(this.lastDrawedSelectionBox, Selection.ZERO_SELECTION)) {
        this.context.clearRect(this.lastDrawedSelectionBox.startX - 1, this.lastDrawedSelectionBox.startY - 1,
            this.lastDrawedSelectionBox.width + 2, this.lastDrawedSelectionBox.height + 2);
        this.lastDrawedSelectionBox = Selection.ZERO_SELECTION;
    }
};

Selection.prototype.isSelectionActive = function() {
    return $(this.canvas).hasClass('active');
};

Selection.prototype.getCurrentSelectionBox = function() {
    return normalize(this.startX, this.startY, this.endX, this.endY);
};

Selection.prototype.handleMouseDown = function(event) {
    if (event.which == 1) {
        $(event.target).parent().parent().addClass('crosshair');
        this.startX = event.pageX - ~~$(event.target).parent().offset().left;
        this.startY = event.pageY - ~~$(event.target).parent().offset().top;

        this.endX = this.startX;
        this.endY = this.startY;

        $(this.canvas).addClass('active');
    }

    return false;
};

Selection.prototype.handleMouseMove = function(event) {
    if (this.isSelectionActive() && event.which == 1) {
        this.endX = event.pageX - ~~$(event.target).parent().offset().left;
        this.endY = event.pageY - ~~$(event.target).parent().offset().top;
    }

    return false;
};

Selection.prototype.handleMouseUp = function(event) {
    if (event.which == 1) {
        $(event.target).parent().parent().removeClass('crosshair');
        $(this.canvas).removeClass('active');

        if (event.shiftKey != 1) {
            this.targets = {};
        }

        var selectionBox = this.getCurrentSelectionBox();
        var matched = this.units.filter(function(o) { return isIntersectOrInside(selectionBox, o.getSelectionBox()); }.bind(this));
        if (matched.length > 1) {
            var groupedByPlayer = groupBy(matched, function(o) { return o.player == this.currentPlayer; }, this);
            if (groupedByPlayer[true]) {
                matched = groupedByPlayer[true].filter(function(o) { return o.type.SelectableByRectangle; });
            } else if (groupedByPlayer[false]) {
                matched = groupedByPlayer[false].slice(0, 1);
            }
        }
        matched.forEach(function(o) { this.targets[o.id] = o; }, this);

        this.selectionListener.fireSelectionChanged();
    }

    return false;
};