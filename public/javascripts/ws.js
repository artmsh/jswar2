$(function() {
    var game = new Game(function() {
        var WS = window['MozWebSocket'] ? MozWebSocket : WebSocket;
        var gameSocket = new WS(location.hostname + "/ws");

        gameSocket.onmessage = function(event) {
            var data = JSON.parse(event);
            game.render(data);
        };

        return gameSocket;
    });
});