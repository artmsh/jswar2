/*
Map layout scheme:
----- selection-active  (z-index: 1001)
----- fog               (z-index: 1000)
----- units             (z-index: [fly: 60, ground: 40, wall: 39, submarine: 35, building: 20])
----- terrain           (z-index: 0)
----- selection-passive (z-index: -1)
*/

function Map(width, height, tileset) {
    this.terrain = create2dArray(height, width, 0);
//    terrain.forEach(function(cell) { this.terrain[cell[0]][cell[1]] = cell[2]; }, this);

    this.width = width;
    this.height = height;

    // 0000b - not seen, 0001b - seen, 0010b - visible now
    //                   0100b - "half"-seen, 1000b - "half"-visible
    this.seenTerrain = create2dArray(height, width, 0);
//    vision.forEach(function(cell) { this.seenTerrain[cell[0]][cell[1]] = cell[2]; }, this);

    this.canvas = $('#map')[0];
    this.context = this.canvas.getContext('2d');

    this.canvas.width = this.width * 32;
    this.canvas.height = this.height * 32;

    this.fogCanvas = $('#fog')[0];
    this.fogContext = this.fogCanvas.getContext('2d');

    this.fogCanvas.width = this.canvas.width;
    this.fogCanvas.height = this.canvas.height;

    this.tileset = tilesets[capitalize(tileset.toLowerCase())];
    this.tileset.indexedTable = [];
    this.tileset.table.forEach(function(e) {
        if (e.slot == 'solid') {
            this.tileset.indexedTable.push(e.table);
        } else {
            if (e.slot == 'mixed') {
                e.table.forEach(function(t) { this.tileset.indexedTable.push(t); }, this);
            }
        }
    }, this);

    ResourcePreloader.add(this.tileset.image);

    var propagatingHandler = function(event) {
        var matchedElements = $(event.target).siblings('canvas').filter(function(index) {
            var mouseX = event.pageX - ~~$(event.target).parent().offset().left;
            var mouseY = event.pageY - ~~$(event.target).parent().offset().top;

            var pos = { left : parseInt($(this).css("margin-left")), top : parseInt($(this).css("margin-top")) };

            return ~~pos.left <= mouseX && (~~pos.left + this.width) >= mouseX &&
                ~~pos.top <= mouseY && (~~pos.top + this.height) >= mouseY; })
            .map(function(index) { return {el: $(this), "z-index": $(this).css('z-index') }; }).get();

        matchedElements.sort(function(a, b) { return b["z-index"] - a["z-index"]; });

        if (matchedElements.length > 0) {
            matchedElements[0].el.trigger(event);
        }
    };
    $(this.fogCanvas).mousedown(propagatingHandler);
    $(this.fogCanvas).mousemove(propagatingHandler);
    $(this.fogCanvas).mouseup(propagatingHandler);
}

Map.prototype.centerViewportToTile = function(x, y) {
    var canvas = $(this.canvas);
    var container = canvas.parent().parent();
    var offsetTop = -(32 * y + 16) + alignTo(Math.floor(container.height() / 2), 32) + 16;
    var offsetLeft = -(32 * x + 16) + alignTo(Math.floor(container.width() / 2), 32) + 16;
    var marginTop = Math.max(Math.min(offsetTop, 0), -(this.canvas.height - container.height()));
    var marginLeft = Math.max(Math.min(offsetLeft, 0), -(this.canvas.width - container.width()));
    canvas.parent().css({"margin-top" : marginTop, "margin-left" : marginLeft});
};

Map.prototype.moveViewportTo = function(dx, dy) {
    var container = $(this.canvas).parent();
    container.css({"margin-top" : parseInt(container.css("margin-top")) - dy * 32,
                   "margin-left" : parseInt(container.css("margin-left")) - dx * 32});
};

Map.prototype.offsetTopPx = function() {
    return -parseInt($(this.canvas).parent().css("margin-top"));
};

Map.prototype.offsetLeftPx = function() {
    return -parseInt($(this.canvas).parent().css("margin-left"));
};

Map.prototype.getSpriteNumByIndex = function(index) {
    var table = this.tileset.indexedTable[index >> 4];

    if (table == undefined) {
        console.log(index);
    }

    return table[index & 0xF];
};

Map.prototype.preDraw = function() {
    var image = ResourcePreloader.get(this.tileset.image);

    this.centerViewportToTile(this.currentPlayer.startX, this.currentPlayer.startY);

    for(var y = 0; y < this.height; y++) {
        for(var x = 0; x < this.width; x++) {
            var seenFlag = this.seenTerrain[y][x] & 5;
            if (seenFlag != 0) {
                var numByIndex = this.getSpriteNumByIndex(this.terrain[y][x]);
                this.context.drawImage(image, (numByIndex % 16) * 32, Math.floor(numByIndex / 16) * 32, 32, 32, x * 32, y * 32, 32, 32);

//                this.context.strokeStyle = "green";
//                this.context.strokeRect(x * 32, y * 32, 32, 32);
//
//                this.context.fillStyle = "black";
//                this.context.font = "14px Arial";
//                this.context.textAlign = "center";
//                this.context.fillText(this.seenTerrain[y][x] + "", x * 32 + 16, y * 32 + 16);
            }
        }
    }
};

Map.prototype.drawFog = function() {
    var image = ResourcePreloader.get(this.tileset.image);

    for(var y = 0; y < this.height; y++) {
        for(var x = 0; x < this.width; x++) {
            var flag = this.seenTerrain[y][x] & 5;
            if (flag == 4) {
                for (var i = 1; i < Map.fogMapping.length; i++) {
                    var notMatch = false;
                    for (var dy = -1; dy <= 1; dy++) {
                        for (var dx = -1; dx <= 1; dx++) {
                            var tile = 0;
                            if (y + dy >= 0 && y + dy < this.height && x + dx >= 0 && x + dx < this.width) {
                                tile = this.seenTerrain[y + dy][x + dx];
                            }
                            var rotatedIndex = Map.rotationMapping[Map.fogMapping[i].rotationAngle].translate(dx + 1, dy + 1);
                            var mappingFlags = Map.fogTileTypes[Map.fogMapping[i].fogTileType][rotatedIndex.y][rotatedIndex.x];
                            if ((tile & mappingFlags) != 0 || tile == 0 && mappingFlags == tile || (tile == 0 && (mappingFlags & 16) == 16)) {

                            } else {
                                notMatch = true;
                                break;
                            }
                        }
                        if (notMatch) break;
                    }

                    if (!notMatch) {
                        this.fogContext.drawImage(image, (i % 16) * 32, Math.floor(i / 16) * 32, 32, 32, x * 32, y * 32, 32, 32);
                        break;
                    }
                }
            } else if (flag == 1) {
                this.fogContext.drawImage(image, 0, 0, 32, 32, x * 32, y * 32, 32, 32);
            } else {
                this.fogContext.fillStyle = "black";
                this.fogContext.fillRect(x * 32, y * 32, 32, 32);
            }
        }
    }
};

Map.fogTileTypes = [
    // 16 means unseen tile
    [
        [20,20,20],
        [20, 4, 4],
        [20, 4, 1]
    ],
    [
        [20,20,20],
        [ 4, 4, 4],
        [ 5, 1, 5]
    ],
    [
        [ 4, 4, 4],
        [ 1, 4, 1],
        [ 4, 4, 4]
    ],
    [
        [20, 4, 5],
        [ 4, 4, 5],
        [ 5, 5, 5]
    ],
    [
        [20, 4, 5],
        [ 4, 4, 4],
        [ 5, 4,20]
    ]
];

Map.rotationMapping = [
    { translate : function(x,y) { return {x : x, y : y}; } },
    { translate : function(x,y) { return {x : y, y : 2 - x}; } },
    { translate : function(x,y) { return {x : 2 - x, y : 2 - y}; } },
    { translate : function(x,y) { return {x : 2 - y, y : x}; } }
];

Map.fogMapping = [
    // first number is tile type, second is rotation angle multiplier, 1 - π/2, 2 - π and so on.
    [],
    // 1
    { fogTileType :  0, rotationAngle : 0},
    // 2
    { fogTileType :  1, rotationAngle : 0},
    // 3
    { fogTileType :  0, rotationAngle : 1},
    // 4
    { fogTileType :  1, rotationAngle : 3},
    // 5
    { fogTileType :  2, rotationAngle : 0},
    // 6
    { fogTileType :  1, rotationAngle : 1},
    // 7
    { fogTileType :  0, rotationAngle : 3},
    // 8
    { fogTileType :  1, rotationAngle : 2},
    // 9
    { fogTileType :  0, rotationAngle : 2},
    // 10
    { fogTileType :  3, rotationAngle : 0},
    // 11
    { fogTileType :  3, rotationAngle : 1},
    // 12
    { fogTileType :  3, rotationAngle : 3},
    // 13
    { fogTileType :  3, rotationAngle : 2},
    // 14
    { fogTileType :  4, rotationAngle : 0},
    // 15
    { fogTileType :  4, rotationAngle : 1}
];