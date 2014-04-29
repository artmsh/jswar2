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

LayoutManager.prototype.addLayer = function(width, height, customParams) {
    var canvasEl;
    if (customParams && customParams.isExist)
        canvasEl = $(customParams.selector);
    else {
        canvasEl = $('<canvas class="' + customParams.className + '"></canvas>');
        canvasEl.css({ "z-index": customParams.zIndex });
        this.container.append(canvasEl);
    }

    if (customParams.topEl) {
        ['mouseup', 'mousemove', 'mousedown'].forEach(function(name) {
            canvasEl.bind(name, propagatingHandler);
        });
    }

    canvasEl.attr({ "width": width, "height": height });

    ['mouseup', 'mouseenter', 'mousemove', 'mouseleave', 'mousedown'].forEach(function(name) {
        if (customParams[name]) canvasEl.bind(name, customParams[name]);
    });

    return { canvas: canvasEl, context: canvasEl[0].getContext('2d') };
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
