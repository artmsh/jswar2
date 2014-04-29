function Minimap(map) {
    this.map = map;

    this.canvas = $('#minimap')[0];
    this.context = this.canvas.getContext('2d');

    this.canvasUnits = $('#minimapUnits')[0];
    this.contextUnits = this.canvasUnits.getContext('2d');

    this.canvasViewportRect = $('#minimapViewportRect')[0];
    this.contextViewportRect = this.canvasViewportRect.getContext('2d');

    this.containerSizeChanged = false;

    $(this.canvas).click(function(event) {
        this.map.centerViewportToTile(event.clientX - this.canvas.offsetLeft,
                                      event.clientY - this.canvas.offsetTop);
    }.bind(this));
    $(window).resize(function(event) { this.containerSizeChanged = true; }.bind(this));
}

Minimap.prototype.preDraw = function(units) {
    this.preLoadTileColors();

    this.draw();
    this.drawUnits(units);
    this.drawViewportRect();
};

Minimap.prototype.preLoadTileColors = function() {
    this.map.tileset.tileToMinimapColor = [];
    this.map.tileset.indexedTable.forEach(function(tablerow) { tablerow.forEach(function(tilenumber) {
        if (!this.map.tileset.tileToMinimapColor[tilenumber]) {
            var tempCanvas = $("<canvas></canvas>")[0];
            tempCanvas.width = 32;
            tempCanvas.height = 32;
            var tempContext = tempCanvas.getContext('2d');
            tempContext.drawImage(ResourcePreloader.get(this.map.tileset.image), (tilenumber % 16) * 32,
                Math.floor(tilenumber / 16) * 32, 32, 32, 0, 0, 32, 32);
            var imageData = tempContext.getImageData(0, 0, 32, 32);
            this.map.tileset.tileToMinimapColor[tilenumber] = [imageData.data[(6 * 32 + 7) * 4],
                imageData.data[(6 * 32 + 7) * 4 + 1], imageData.data[(6 * 32 + 7) * 4 + 2]];
        }
    }.bind(this)); }.bind(this));
};

Minimap.prototype.drawTiles = function(tiles) {
    tiles.forEach(function(tile) {
        var numByIndex = this.map.getSpriteNumByIndex(tile.tile);
        this.context.fillStyle = rgbToCss(this.map.tileset.tileToMinimapColor[numByIndex]);
        this.context.fillRect(tile.x, tile.y, 1, 1);
    }.bind(this));

//    for(var y = 0; y < this.map.height; y++) {
//        for(var x = 0; x < this.map.width; x++) {
//            var seenFlag = this.map.seenTerrain[y][x] & 5;
//            if (seenFlag != 0) {
//                var numByIndex = this.map.getSpriteNumByIndex(this.map.terrain[y][x]);
//                this.context.fillStyle = rgbToCss(this.map.tileset.tileToMinimapColor[numByIndex]);
//            } else {
//                this.context.fillStyle = "black";
//            }
//            this.context.fillRect(x, y, 1, 1);
//        }
//    }
};

Minimap.prototype.drawUnits = function(units) {
    units.filter(function(unit) {
        var color;
        if (unit.race == Race.NEUTRAL) {
//            color = unit.type.NeutralMinimapColor;
            color = "yellow";
        } else if (unit.playerNum == this.map.currentPlayer.number) {
            color = Colors.Green;
        } else {
            color = playerColors[unit.playerNum][0];
        }

        this.contextUnits.fillStyle = rgbToCss(color);
        this.contextUnits.fillRect(unit.x, unit.y, unit.type.TileSize[0], unit.type.TileSize[1]);
    }, this);
};

Minimap.prototype.drawViewportRect = function() {
    this.contextViewportRect.strokeStyle = "white";
//    var container = $(this.map.canvas).parent().parent();
    var scaleX = this.map.width * 32 / 128;
    var scaleY = this.map.height * 32 / 128;
    var layout = this.map.layout;
    this.contextViewportRect.strokeRect(layout.getViewportOffsetX() / scaleX, layout.getViewportOffsetY() / scaleY,
        layout.getViewportWidth() / scaleX, layout.getViewportHeight() / scaleY);
};