function Layout(width, height, id, className, zIndex) {
    this.canvasEl = $('<canvas></canvas>');
    this.canvasEl.attr({ id: id, 'class': className, 'width': width, 'height': height });
    this.canvasEl.css({ 'z-index': zIndex });

    this.context = this.canvasEl[0].getContext('2d');
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
    this.canvasEl.bind(event, callback);
};