var Race = { DEFAULT : "Map Default", RANDOM : "Random", ORC : "Orc", HUMAN : "Human", NEUTRAL: "Neutral" };
var Resources = { DEFAULT : "Map Default", RANDOM : "Random", LOW : "Low", MEDIUM : "Medium", HIGH : "High" };
var Units = { DEFAULT : "Map Default", PEASANT_ONLY : "One Peasant Only" };
var Opponents = { DEFAULT : "Map Default" };
for (var i = 1; i <= 7; i++) Opponents["OP" + i] = i;
var Tileset = { DEFAULT : "Map Default", SUMMER : "Summer", SWAMP : "Swamp", WASTELAND : "Wasteland", WINTER : "Winter" };

var PlayerType = ["Neutral", "Nobody", "Computer", "Person", "RescuePassive", "RescueActive"];

var playerColors = [
    [[164, 0, 0], [124, 0, 0], [92, 4, 0], [68, 4, 0]], // red
    [[12, 72, 204], [4, 40, 160], [0, 20, 116], [0, 4, 76]], // blue
    [[44, 180, 148], [20, 132, 92], [4, 84, 44], [0, 40, 12]], // green
    [[152, 72, 176], [116, 44, 132], [80, 24, 88], [44, 8, 44]], // violet
    [[248, 140, 20], [200, 96, 16], [152, 60, 16], [108, 32, 12]], // orange
    [[40, 40, 60], [28, 28, 44], [20, 20, 32], [12, 12, 20]], // black
    [[224, 224, 224], [152, 152, 180], [84, 84, 128], [36, 40, 76]], // white
    [[252, 252, 72], [228, 204, 40], [204, 160, 16], [180, 116, 0]]  // yellow
];

var Colors = {
    White : [252, 248, 240],
    Green : [0, 252, 0]
};

var Directions = [
    [0, -1],
    [1, -1],
    [1, 0],
    [1, 1],
    [0, 1],
    [-1, 1],
    [-1, 0],
    [-1, -1]
];