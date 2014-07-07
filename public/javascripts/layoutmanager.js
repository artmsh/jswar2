/*
 Map layout scheme:
 ----- selection-active  (z-index: 1001)
 ----- fog               (z-index: 1000)
 ----- missiles          (z-index: 50-100)
 ----- units             (z-index: [fly: 60, ground: 40, wall: 39, submarine: 35, building: 20])
 ----- terrain           (z-index: 0)
 ----- selection-passive (z-index: -1)
 */

function LayoutManager() {
    this.mapContainer = $('#mapContainer');
    this.container = this.mapContainer.children('div');
    this.layouts = [];
}

/**
 * @param _x top left x coordinate
 * @param _y top left y coordinate
 *
 * #mapContainer > div
 * -----------------------------------
 * |                                 |
 * |                                 |
 * | #mapContainer (x,y)             |
 * |                 ----            |
 * |                 |  |            |
 * |                 |  |            |
 * |                 ----            |
 * -----------------------------------
 */
LayoutManager.prototype.moveViewport = function(_x, _y) {
    var x = Math.max(_x, 0);
    var y = Math.max(_y, 0);

    this.container.css({
        'margin-left': -Math.min(x, this.container.find('#map').width() - this.mapContainer.width()),
        'margin-top': -Math.min(y, this.container.find('#map').height() - this.mapContainer.height())
    });
};

LayoutManager.prototype.getViewportOffsetX = function() { return -parseInt(this.container.css('margin-left')); };
LayoutManager.prototype.getViewportOffsetY = function() { return -parseInt(this.container.css('margin-top')); };

LayoutManager.prototype.getViewportWidth = function() { this.mapContainer.width(); };
LayoutManager.prototype.getViewportHeight = function() { this.mapContainer.height(); };

LayoutManager.prototype.createLayout = function(width, height, id, className, zIndex) {
    var layout = new Layout(width, height, className, zIndex);

    this.container.append(layout.canvasEl);

    ['mouseup', 'mouseenter', 'mouseleave', 'mousedown'].forEach(function(name) {
        layout.canvasEl.bind(name, function(e) { console.log(e); });
    });

    return layout;
};

function propagatingHandler(event) {
    var matchedElements = $(event.target).siblings('canvas').filter(function(index) {
        var mouseX = event.pageX - ~~$(event.target).parent().offset().left;
        var mouseY = event.pageY - ~~$(event.target).parent().offset().top;

        var pos = { left : parseInt($(this).css("margin-left")), top : parseInt($(this).css("margin-top")) };

        return ~~pos.left <= mouseX && (~~pos.left + this.width) >= mouseX &&
            ~~pos.top <= mouseY && (~~pos.top + this.height) >= mouseY; })
        .map(function(index) { return {el: $(this), "z-index": $(this).css('z-index') }; }).get();

    matchedElements.sort(function(a, b) { return b["z-index"] - a["z-index"]; });

    if (matchedElements.length > 0) {
        matchedElements[0].el.trigger(event);
    }
}
