function SelectionListener() {
    this.listeners = [];
}

SelectionListener.prototype.addListener = function(listener) {
    this.listeners.push(listener);
};

SelectionListener.prototype.fireSelectionChanged = function() {
    this.listeners.forEach(function(listener) { listener(); });
};