var tilesets = {};
var units = {};
var UnitTypeFiles = {};
var animations = {};
var buttons = [];
var missiles = {};
var burningBuilding = [];

function Game(wsCallback) {
    this.map = new Map(mapWidth, mapHeight, mapTileset);
    this.minimap = new Minimap(this.map, minimapEl);

    this.units = data.units.map(function(unit) { return new Unit(unit); });
//    mapData.units.filter(function(ud) { return this.players[ud.player] !== undefined; }, this).forEach(function(ud) {
//        var unitScriptName = unitScriptNames[ud.type];
//        var p = this.players[ud.player];
//        if (unitScriptName == 'unit-human-start-location' || unitScriptName == 'unit-orc-start-location') {
//            p.startView = {x : ud.x, y: ud.y};
//        } else {
//            this.units.push(new Unit(ud.x, ud.y, unitScriptName, p, ud.data, Game.getUnitTypeImage(unitScriptName, this.map.tileset.name)));
//        }
//    }, this);

    $(mapEl).parents("#container").addClass(data.player.race.toLowerCase());

    this.selectionListener = new SelectionListener();
    this.selection = new Selection(selectionEl, this.map, data.player, data.units, "green", this.selectionListener);
    this.infopanel = new InfoPanel(infopanelEl, this.selection, data.player.race, data.tileset);

    this.selectionListener.addListener(this.infopanel.onSelectionChanged.bind(this.infopanel));

    this.frames = 0;

    // preload all needed images
    ResourcePreloader.add(this.map.tileset.image);
    unitScriptNames.filter(function(s) { return s != ''; }).forEach(function(u) { ResourcePreloader.add(Game.getUnitTypeImage(u, this.map.tileset.name)); }, this);
    $.each(missiles, function(name, missile) { if (missile.File) { ResourcePreloader.add('assets/images/' + missile.File); } });

    this.gameSocket = wsCallback();

    $(keyboardHandlerEl).keydown(this.handleKeyEvent.bind(this));
}

Game.getUnitTypeImage = function(unitScriptName, tilesetName) {
    var type = units[unitScriptName];

    return 'assets/images/' + (type.Image.file ? type.Image.file : UnitTypeFiles[unitScriptName][tilesetName]);
};

Game.prototype.handleKeyEvent = function(event) {
    var key = keymap[event.which];
    if (key) {
        var container = $(this.map.canvas).parent().parent();
        var dx = 0, dy = 0;
        if (key == 'down' && this.map.offsetTopPx() < this.map.canvas.height - container.height()) dy = 1;
        if (key == 'up' && this.map.offsetTopPx() > 0) dy = -1;
        if (key == 'left' && this.map.offsetLeftPx() > 0) dx = -1;
        if (key == 'right' && this.map.offsetLeftPx() < this.map.canvas.width - container.width()) dx = 1;

        this.map.moveViewportTo(dx, dy);
    }
};

Game.prototype.loadMap = function(url) {
    var _data;

    $.ajax({
        url: url,
        dataType: "json",
        context: this,
        async: false,
        success: function(data) {
            _data = data;
        },
        error: function() {
            alert('Error while loading ' + url);
        }
    });

    return _data;
};

Game.prototype.draw = function() {
    ResourcePreloader.loadAll(function() {
        this.map.preDraw();
        this.minimap.preDraw(this.units);

        this.units.filter(function(u) {
            u.draw($(this.map.canvas).parent()[0], this.currentPlayer);

//            var center = u.getCenterCoords();
//            this.map.context.beginPath();
//            this.map.context.strokeStyle = "green";
//            this.map.context.arc(center.x, center.y, u.type.SightRange * 32 + u.getTypeTileHeight() * 16, 0, Math.PI * 2);
//            this.map.context.stroke();

        }, this);

        this.map.drawFog();

        this.gameLoop();
    }.bind(this));
};

Game.prototype.gameLoop = function() {
    this.gameSocket.send(JSON.stringify(this.actionEvents));
    this.actionEvents = [];
    requestAnimFrame(this.gameLoop.bind(this), this.map.canvas);
};

/**
 * Render update received from server
 */
Game.prototype.render = function(data) {
//    this.infopanel.draw();
    this.selection.redraw();

//    if (oldMinimapViewportState[0] != $(this.mapCanvas).parent().css("margin-left") ||
//        oldMinimapViewportState[1] != $(this.mapCanvas).parent().css("margin-top") ||
//        containerSizeChanged) {
//        this.redrawMinimap();
//    }
//
//    this.world.units.forEach(function(unit) {
////        unit.updateAnimation();
//        unit.executeAction();
//        unit.redrawIfNeeded(this.mapContext, this.selection.targets[unit.id] != undefined);
//    }.bind(this));
};