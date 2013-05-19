function JsCore() {

}

JsCore.prototype.loadInitialData = function() {
};

function ScalaCore() {

}

ScalaCore.prototype.loadInitialData = function(race, resources, units, opponents, tileset, mapname, callback) {
    $.get("data", {race : nameOf(Race, race), resources : nameOf(Resources, resources), units : nameOf(Units, units),
            opponents : nameOf(Opponents, opponents), tilesetName : nameOf(Tileset, tileset), mapname : mapname}, function(data) {
        callback.call(null, data);
    });
};