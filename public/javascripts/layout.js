function Layout(width, height, id, className, zIndex, container) {
    this.canvasEl = $('<canvas></canvas>');
    this.canvasEl.attr({ id: id, 'class': className, 'width': width, 'height': height });
    this.canvasEl.css({ 'z-index': zIndex });

    this.context = this.canvasEl[0].getContext('2d');
    this.container = container;
}

Layout.prototype.containsPoint = function(x, y) {

};

/**
 * Subscribe to given Mouse event.
 *
 * @param event [mouseup | mouseenter | mousemove | mouseleave | mousedown | click]
 * @param callback event handler
 */
Layout.prototype.on = function(event, callback) {
    var _this = this;
    this.canvasEl.bind(event, function(e) {
        var x = e.pageX - _this.container.viewportOffsetX();
        var y = e.pageY - _this.container.viewportOffsetY();

        callback(x, y, e);
    });
};