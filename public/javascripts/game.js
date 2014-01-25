// Main function

function Game(gameSocket) {
    this.gameSocket = gameSocket;

    this.frames = 0;
    this.units = {};

    this.actionEvents = [];
    $("body").keydown(this.handleKeyEvent.bind(this));
}

Game.prototype.init = function(playerNum, race, unitTypes, startX, startY) {
    this.playerNum = playerNum;
    this.unitTypes = unitTypes;
    this.race = race;
    $('#container').addClass(race);

    this.map = new Map(mapWidth, mapHeight, mapTileset);
    this.minimap = new Minimap(this.map);

    this.selectionListener = new SelectionListener();
    this.selection = new Selection(this.map, "green", this.selectionListener, this);
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

    this.map.centerViewportToTile(this.startX, this.startY);

    // preload all needed images
    var tileset = tilesets[capitalize(mapTileset.toLowerCase())];
    ResourcePreloader.add(tileset.image);

    Object.keys(unitTypes).filter(function(s) { return s != ''; }).forEach(function(u) {
        ResourcePreloader.add(Game.getUnitTypeImage(u, tileset.name));
        if (unitTypes[u].missile) ResourcePreloader.add('assets/images/missiles/' + unitTypes[u].missile + '.png');
    }, this);

    ResourcePreloader.loadAll(function(image) {
//            $('.status-line').text(image);

        },
    function() {
        this.minimap.preLoadTileColors();

        this.gameLoop();
    }.bind(this));
};

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
            var tileCoords = this.map.toTileCoords(
                event.pageX - parseInt($(event.target).parent().offset().left),
                event.pageY - parseInt($(event.target).parent().offset().top));
            if (checkTerrain.bind(this)(tileCoords)) {
                this.actionEvents.push({ name: 'move', unit: Object.keys(this.selection.targets)[0], x: tileCoords[0], y: tileCoords[1] });
            }

            return false;
        }
    }
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


/**
* Handle Update commands
*/
Game.prototype.onUpdate = function(event) {
    var data = JSON.parse(event.data);
    this.map.updateTerrain(data.addedTerrain);
    this.map.drawTiles(data.addedTerrain);
    this.map.drawTiles(data.changedTerrain);
    this.map.drawFog();

    this.minimap.drawTiles(data.addedTerrain);
    this.minimap.drawTiles(data.changedTerrain);
    this.minimap.drawViewportRect();

    for (var unitId in data.addedUnits) {
        this.units[unitId] = new Unit(data.addedUnits[unitId], $(this.map.canvas).parent()[0]);
        this.units[unitId].image = Game.getUnitTypeImage(data.addedUnits[unitId].name, this.map.tileset.name);
        this.units[unitId].draw(this.playerNum);
    }
};

Game.prototype.gameLoop = function() {
    if (this.actionEvents.length > 0) {
        this.gameSocket.send(JSON.stringify(this.actionEvents));
    }
    this.actionEvents = [];

    this.selection.redraw();
    Object.keys(this.units).forEach(function(key) {
        var unit = this.units[key];
        ////        unit.updateAnimation();
//        unit.executeAction();
        unit.redrawIfNeeded(this.playerNum, this.selection.targets[key] != undefined);
    }, this);

    this.frames++;
    requestAnimFrame(this.gameLoop.bind(this), this.map.canvas);
};

/**
 * Render update received from server
 */
Game.prototype.render = function(data) {
//    this.infopanel.draw();

//    if (oldMinimapViewportState[0] != $(this.mapCanvas).parent().css("margin-left") ||
//        oldMinimapViewportState[1] != $(this.mapCanvas).parent().css("margin-top") ||
//        containerSizeChanged) {
//        this.redrawMinimap();
//    }
//
};