var FootmanStill = UnitStill;
var FootmanMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"];
var FootmanDeath = ["unbreakable begin", "frame 45", "wait 3", "frame 50", "wait 3", "frame 55", "wait 100",
    "frame 55", "unbreakable end", "wait 1"];

animations["animations-footman"] = {
    Still : FootmanStill,
    Move : FootmanMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound footman-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : FootmanDeath
};

animations["animations-arthor-literios"] = {
    Still : FootmanStill,
    Move : FootmanMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound danath-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : FootmanDeath
};


animations["animations-peasant"] = {
    Still : UnitStill,
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
        "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
        "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
        "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
        "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound peasant-attack", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Harvest_wood : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "sound tree-chopping", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Repair : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "sound peasant-attack", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 50", "wait 3", "frame 55", "wait 3", "frame 60", "wait 100",
        "frame 60", "unbreakable end", "wait 1"]
};


animations["animations-ballista"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin","frame 0", "wait 1", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 5", "move 2", "wait 2",
        "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 10", "wait 25",
        "frame 15", "attack", "sound ballista-attack", "wait 25", "frame 15", "wait 100",
        "frame 0", "wait 49", "frame 0", "unbreakable end", "wait 1"]
};


//
// knight, paladin, knight-rider, wise-man, and man-of-light
//

var KnightStill = UnitStill;
var KnightMove = ["unbreakable begin","frame 0", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 5", "move 4", "wait 2", "frame 10", "move 3", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 15", "move 3", "wait 1",
    "frame 15", "move 4", "wait 2", "frame 20", "move 3", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"];
var KnightDeath = ["unbreakable begin", "frame 45", "wait 3", "frame 50", "wait 3", "frame 55", "wait 100",
    "frame 60", "wait 200", "frame 65", "wait 200", "frame 65", "unbreakable end", "wait 1"];

animations["animations-knight"] = {
    Still : KnightStill,
    Move : KnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound knight-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : KnightDeath
};

animations["animations-paladin"] = {
    Still : KnightStill,
    Move : KnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound paladin-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : KnightDeath
};

animations["animations-knight-rider"] = {
    Still : KnightStill,
    Move : KnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound turalyon-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : KnightDeath
};

animations["animations-wise-man"] = {
    Still : KnightStill,
    Move : KnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound lothar-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : KnightDeath
};

animations["animations-man-of-light"] = {
    Still : KnightStill,
    Move : KnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound uther-lightbringer-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : KnightDeath
};


//
// archer, ranger, and female-hero
//

var ArcherStill = UnitStill;
var ArcherMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"];
var ArcherDeath = ["unbreakable begin", "frame 35", "wait 3", "frame 40", "wait 3", "frame 45", "wait 100",
    "frame 45", "unbreakable end", "wait 1"];

animations["animations-archer"] = {
    Still : ArcherStill,
    Move : ArcherMove,
    Attack : ["unbreakable begin", "frame 25", "wait 10", "frame 30", "attack", "sound archer-attack", "wait 10",
        "frame 0", "wait 44", "frame 0", "unbreakable end", "wait 1"],
    Death : ArcherDeath
};

animations["animations-ranger"] = {
    Still : ArcherStill,
    Move : ArcherMove,
    Attack : ["unbreakable begin", "frame 25", "wait 10", "frame 30", "attack", "sound ranger-attack", "wait 10",
        "frame 0", "wait 44", "frame 0", "unbreakable end", "wait 1"],
    Death : ArcherDeath
};

animations["animations-female-hero"] = {
    Still : ArcherStill,
    Move : ArcherMove,
    Attack : ["unbreakable begin", "frame 25", "wait 10", "frame 30", "attack", "sound alleria-attack", "wait 10",
        "frame 0", "wait 44", "frame 0", "unbreakable end", "wait 1"],
    Death : ArcherDeath
};


//
// mage and white-mage
//

var MageStill = UnitStill;
var MageMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 2", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 2", "frame 0", "move 2", "unbreakable end", "wait 1"];
var MageDeath = ["unbreakable begin", "frame 45", "wait 5", "frame 50", "wait 5", "frame 55", "wait 5",
    "frame 60", "wait 5", "frame 65", "wait 5", "frame 70", "wait 5",
    "frame 75", "wait 5", "frame 75", "unbreakable end", "wait 1"];

animations["animations-mage"] = {
    Still : MageStill,
    Move : MageMove,
    Attack : ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5",
        "frame 35", "attack", "sound mage-attack", "wait 7",
        "frame 40", "wait 5", "frame 0", "wait 17", "frame 0", "unbreakable end", "wait 1"],
    Death : MageDeath
};

animations["animations-white-mage"] = {
    Still : MageStill,
    Move : MageMove,
    Attack : ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5",
        "frame 35", "attack", "sound khadgar-attack", "wait 7",
        "frame 40", "wait 5", "frame 0", "wait 17", "frame 0", "unbreakable end", "wait 1"],
    Death : MageDeath
};


animations["animations-dwarves"] = {
    Still : UnitStill,
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 2", "frame 10", "move 3", "wait 1",
        "frame 10", "move 4", "wait 2", "frame 25", "move 3", "wait 1",
        "frame 25", "move 3", "wait 2", "frame 40", "move 3", "wait 1",
        "frame 40", "move 4", "wait 2", "frame 55", "move 3", "wait 1",
        "frame 55", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 15", "wait 3",
        "frame 30", "attack", "sound dwarves-attack", "wait 5",
        "frame 45", "wait 3", "frame 0", "wait 13", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 3", "frame 20", "wait 3", "frame 35", "wait 3",
        "frame 50", "wait 3", "frame 60", "wait 3", "frame 60", "unbreakable end", "wait 1"]
};


//
// gryphon-rider and flying-angel
//

var GryphonRiderStill = ["frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
    "frame 15", "wait 6"];
var GryphonRiderMove = ["unbreakable begin", "frame 0", "wait 1", "frame 0", "move 3", "wait 1",
    "frame 0", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 5", "move 2", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 10", "move 3", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"];
var GryphonRiderDeath = ["unbreakable begin", "frame 35", "wait 5", "frame 40", "wait 5", "frame 45", "wait 5",
    "frame 50", "wait 5", "frame 55", "wait 5", "frame 60", "wait 5",
    "frame 60", "unbreakable end", "wait 1"];

animations["animations-gryphon-rider"] = {
    Still : GryphonRiderStill,
    Move : GryphonRiderMove,
    Attack : ["unbreakable begin", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 15", "wait 1",
        "frame 20", "wait 6", "frame 25", "wait 6",
        "frame 30", "attack", "sound gryphon-rider-attack", "wait 8",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "unbreakable end", "wait 1"],
    Death : GryphonRiderDeath
};

animations["animations-flying-angel"] = {
    Still : GryphonRiderStill,
    Move : GryphonRiderMove,
    Attack : ["unbreakable begin", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 15", "wait 1",
        "frame 20", "wait 6", "frame 25", "wait 6",
        "frame 30", "attack", "sound kurdan-and-sky-ree-attack", "wait 8",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "unbreakable end", "wait 1"],
    Death : GryphonRiderDeath
};


animations["animations-human-oil-tanker"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 30",
        "frame 0", "wait 99", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


animations["animations-human-transport"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 119",
        "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


animations["animations-elven-destroyer"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "sound elven-destroyer-attack", "wait 119",
        "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


animations["animations-battleship"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "sound battleship-attack", "wait 127",
        "frame 0", "wait 102", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


animations["animations-gnomish-submarine"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 5", "wait 10", "frame 10", "wait 25",
        "frame 10", "attack", "sound gnomish-submarine-attack", "wait 25",
        "frame 5", "wait 25", "frame 0", "wait 29", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"]
};


animations["animations-balloon"] = {
    Still : ["frame 0", "wait 1", "frame 5", "wait 1", "frame 5", "wait 1",
    "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 5", "move 4", "wait 1", "frame 5", "move 3", "wait 1",
        "frame 5", "move 3", "wait 1", "frame 0", "move 3", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 5", "move 4", "wait 1",
        "frame 5", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"]
};


animations["animations-human-guard-tower"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 59",
        "frame 0", "unbreakable end", "wait 1"]
};

////////
    //	Cannon Tower, Cannon Tower
animations["animations-human-cannon-tower"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 150",
        "frame 0", "unbreakable end", "wait 1"]
};