// Main function

function Game(gameSocket) {
    this.gameSocket = gameSocket;

    this.frames = 0;
    this.units = {};
    this.missiles = [];
    this.clientMissiles = [];

    $("body").keydown(this.handleKeyEvent.bind(this));
}

Game.debug = true;

Game.prototype.init = function(playerNum, race, unitTypes, startX, startY, onCompleteCallback) {
    this.playerNum = playerNum;
    this.unitTypes = unitTypes;
    this.race = race;
    $('#container').addClass(race);

    this.layout = new LayoutManager();

    this.map = new Map(mapWidth, mapHeight, mapTileset, this);
    this.minimap = new Minimap(this.map);

    this.selectionListener = new SelectionListener();
    this.selection = new Selection(this.map, "green", this.selectionListener, this.units, this.layout, playerNum);
    this.infopanel = new InfoPanel(this.selection, mapTileset);

    $(this.map.canvas).mousedown(this.handleMouseEvent.bind(this));

    this.selectionListener.addListener(this.infopanel.onSelectionChanged.bind(this.infopanel));
//    this.selectionListener.addListener(function() {
//        Object.keys(this.units).forEach(function(unitId) {
//            if (this.selection.targets[unitId]) {
//                if (!this.units[unitId].selected) {
//                    this.units[unitId].selected = true;
//                }
//            } else {
//                if (this.units[unitId].selected) {
//                    this.units[unitId].selected = false;
//                }
//            }
//        }.bind(this));
//    }.bind(this));

    this.map.centerViewportToTile(startX, startY);

    // preload all needed images
    var tileset = tilesets[capitalize(mapTileset.toLowerCase())];
    ResourcePreloader.add(tileset.image);

    Object.keys(unitTypes).filter(function(s) { return s != ''; }).forEach(function(u) {
        ResourcePreloader.add(Game.getUnitTypeImage(u, tileset.name));
        if (unitTypes[u].missile) ResourcePreloader.add('/assets/images/missiles/' + unitTypes[u].missile + '.png');
    }, this);

    ResourcePreloader.add('/assets/images/missiles/green_cross.png');

    ResourcePreloader.loadAll(function(image) {
//            $('.status-line').text(image);

        },
    function() {
        this.minimap.preLoadTileColors();
        onCompleteCallback();

        this.gameLoop();
    }.bind(this));
};

Game.getUnitTypeImage = function(unitScriptName, tilesetName) {
    var type = units[unitScriptName];

    return '/assets/images/' + (type.Image.file ? type.Image.file : UnitTypeFiles[unitScriptName][tilesetName]);
};

Game.prototype.handleKeyEvent = function(event) {
    var key = keymap[event.which];
    if (key) {
        var dx = 0, dy = 0;
        if (key == 'down') dy = 1;
        if (key == 'up') dy = -1;
        if (key == 'left') dx = -1;
        if (key == 'right') dx = 1;

        this.map.moveViewportTo(dx, dy);
    }
};

Game.prototype.handleMouseEvent = function(event) {
    function checkSelection(selectedUnitIds) {
        var selectedUnits = selectedUnitIds.map(function(key) { return this.units[key]; }, this);
        // todo support for multiple targets
        return selectedUnits.length == 1 && selectedUnits.some(function(u) { return u.player == this.playerNum; }, this);
    }

    function checkTerrain(tileCoords) {
        // todo care of land, air and water units
        var tile = this.map.terrain[tileCoords[1]][tileCoords[0]];
        return (tile & 0xFF30) == 0x0030 || (tile & 0xFF40) == 0x0040 ||
            (tile & 0xFF50) == 0x0050 || (tile & 0xFF60) == 0x0060 ||
            (tile & 0xF300) == 0x0300 || (tile & 0xF500) == 0x0500 || (tile & 0xF600) == 0x0600;
    }

    switch (event.which) {
        case 3: if (checkSelection.bind(this)(Object.keys(this.selection.targets))) {
            var mapX = event.pageX - parseInt($(event.target).parent().offset().left);
            var mapY = event.pageY - parseInt($(event.target).parent().offset().top);
            var tileCoords = this.map.toTileCoords(mapX, mapY);

            var greenCross = new Missile(mapX, mapY, 'missile-green-cross', this.layout);
            this.missiles.push(greenCross);

            if (checkTerrain.bind(this)(tileCoords)) {
                this.gameSocket.send(JSON.stringify([{
                    name: 'move',
                    unit: Object.keys(this.selection.targets)[0],
                    x: tileCoords[0],
                    y: tileCoords[1]
                }]));
            }

            return false;
        }
    }
};

/**
* Handle Update commands from WS
*/
Game.prototype.onUpdate = function(event) {
//    console.info(this.frames);
    Object.keys(this.units).forEach(function(key) {
        var unit = this.units[key];
        unit.animateAndRedraw(this.playerNum, this.selection.targets[key] != undefined);
//        unit.executeAction();
    }, this);

    this.missiles.forEach(function(missile) { missile.redrawIfNeeded(); });
    this.missiles = this.missiles.filter(function(missile) { if (missile.isDone()) { missile.detach(); return false; } else return true; });

    var data = JSON.parse(event.data);
    var addedTerrain = data.addedTerrain || [];
    var changedTerrain = data.changedTerrain || [];
    var addedUnits = data.addedUnits || {};
    var updatedUnits = data.updatedUnits || {};

    this.map.updateTerrain(addedTerrain);
    this.map.drawTiles(addedTerrain);
    this.map.drawTiles(changedTerrain);
    if (addedTerrain.length > 0 || changedTerrain.length > 0) {
        this.map.drawFog();
    }

    this.minimap.drawTiles(addedTerrain);
    this.minimap.drawTiles(changedTerrain);
    this.minimap.drawViewportRect();

    for (var unitId in addedUnits) {
        var addedUnit = addedUnits[unitId];
        this.units[unitId] = new Unit(addedUnit, this.layout, this.selection, this.unitTypes[addedUnit.name]);
        this.units[unitId].image = Game.getUnitTypeImage(addedUnit.name, this.map.tileset.name);
        this.units[unitId].draw(this.playerNum);
    }

    for (var unitId in updatedUnits) {
        var changeSet = updatedUnits[unitId];
        console.info(this.frames, changeSet);
        this.units[unitId].applyChangeset(changeSet);
        this.units[unitId].draw(this.playerNum);
    }

//    this.frames++;
};

Game.prototype.gameLoop = function() {
    this.frames++;

    this.selection.redraw();

    this.clientMissiles.forEach(function(missile) { missile.redrawIfNeeded(); });
    this.clientMissiles = this.clientMissiles.filter(function(missile) { if (missile.isDone()) { missile.detach(); return false; } else return true; });

    requestAnimFrame(this.gameLoop.bind(this), this.map.canvas);
};
