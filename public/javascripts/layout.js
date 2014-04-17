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
    this.container = $('#mapContainer').children('div');
}

LayoutManager.prototype.addLayer = function(width, height, customParams) {
    var canvasEl;
    if (customParams && customParams.isExist)
        canvasEl = $(customParams.selector);
    else {
        canvasEl = $('<canvas class="' + customParams.className + '"></canvas>');
        canvasEl.css({ "z-index": customParams.zIndex });
    }

    canvasEl.attr({ "width": width, "height": height });

    ['mouseup', 'mouseenter', 'mousemove', 'mouseleave', 'mousedown'].forEach(function(name) {
        if (customParams[name]) canvasEl.bind(name, customParams[name]);
    });

    this.container.add(canvasEl);

    return canvasEl[0].getContext('2d');
};