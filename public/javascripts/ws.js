$(function() {
    var game = new Game(function() {
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
        var gameSocket = new WS("ws://localhost:9000/ws");

        gameSocket.ready = $.Deferred();
        gameSocket.onopen = function(event) {
            this.ready.resolve();
        };

        gameSocket.send = function(text) {
            this.ready.done(function() {
                WS.prototype.send.call(this, text);
            }.bind(this));
        };

        gameSocket.onmessage = function(event) {
            var data = JSON.parse(event.data);
            game.render(data);
        };

        return gameSocket;
    });
});