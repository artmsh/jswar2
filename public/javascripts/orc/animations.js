var GruntStill = UnitStill;
var GruntMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"];
var GruntDeath = ["unbreakable begin", "frame 45", "wait 3", "frame 50", "wait 3", "frame 55", "wait 100",
    "frame 55", "unbreakable end", "wait 1"];

animations["animations-grunt"] = {
    Still : GruntStill,
    Move : GruntMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound grunt-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : GruntDeath
};

animations["animations-beast-cry"] = {
    Still : GruntStill,
    Move : GruntMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound grom-hellscream-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : GruntDeath
};

animations["animations-quick-blade"] = {
    Still : GruntStill,
    Move : GruntMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound korgath-bladefist-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : GruntDeath
};


animations["animations-peon"] = {
    Still : UnitStill,
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
        "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
        "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
        "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
        "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound peon-attack", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Harvest_wood : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "sound tree-chopping", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Repair : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "sound peon-attack", "wait 5", "frame 45", "wait 3",
        "frame 25", "wait 7", "frame 25", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 50", "wait 3", "frame 55", "wait 3", "frame 60", "wait 100",
        "frame 60", "unbreakable end", "wait 1"]
};


animations["animations-catapult"] = {
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
    Attack : ["unbreakable begin", "frame 15", "attack", "sound catapult-attack","wait 4",
        "frame 10", "wait 4", "frame 15", "wait 3", "frame 10", "wait 2", "frame 15", "wait 2",
        "frame 10", "wait 30", "frame 15", "wait 4", "frame 15", "wait 100", "frame 0", "wait 50", "frame 0", "unbreakable end", "wait 1"]
};


//
    // ogre, ogre-mage, fad-man, and double-head
//

var OgreStill = UnitStill;
var OgreMove = ["unbreakable begin","frame 0", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 1", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 1", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 1", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"];
var OgreDeath = ["unbreakable begin", "frame 45", "wait 3", "frame 50", "wait 3", "frame 55", "wait 100",
    "frame 60", "wait 200", "frame 65", "wait 200", "frame 65", "unbreakable end", "wait 1"];

animations["animations-ogre"] = {
    Still : OgreStill,
    Move : OgreMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound ogre-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : OgreDeath
};

animations["animations-ogre-mage"] = {
    Still : OgreStill,
    Move : OgreMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound ogre-mage-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : OgreDeath
};

animations["animations-fad-man"] = {
    Still : OgreStill,
    Move : OgreMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound dentarg-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : OgreDeath
};

animations["animations-double-head"] = {
    Still : OgreStill,
    Move : OgreMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound cho-gall-attack", "wait 5", "frame 0", "wait 10",
        "frame 0", "unbreakable end", "wait 1"],
    Death : OgreDeath
};


//
    // axethrower, berserker, and sharp-axe
//

var AxeThrowerStill = UnitStill;
var AxeThrowerMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 2", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 0", "move 2", "wait 1",
    "frame 0", "move 3", "wait 2", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 2", "frame 20", "move 2", "wait 1",
    "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"];
var AxeThrowerDeath = ["unbreakable begin", "frame 45", "wait 3", "frame 50", "wait 3", "frame 55", "wait 100",
    "frame 55", "unbreakable end", "wait 1"];

animations["animations-axethrower"] = {
    Still : AxeThrowerStill,
    Move : AxeThrowerMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound axethrower-attack", "wait 12",
        "frame 0", "wait 52", "frame 0", "unbreakable end", "wait 1"],
    Death : AxeThrowerDeath
};

animations["animations-berserker"] = {
    Still : AxeThrowerStill,
    Move : AxeThrowerMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound berserker-attack", "wait 12",
        "frame 0", "wait 52", "frame 0", "unbreakable end", "wait 1"],
    Death : AxeThrowerDeath
};

animations["animations-sharp-axe"] = {
    Still : AxeThrowerStill,
    Move : AxeThrowerMove,
    Attack : ["unbreakable begin", "frame 25", "wait 3", "frame 30", "wait 3", "frame 35", "wait 3",
        "frame 40", "attack", "sound zuljin-attack", "wait 12",
        "frame 0", "wait 52", "frame 0", "unbreakable end", "wait 1"],
    Death : AxeThrowerDeath
};


//
    // death-knight, evil-knight, and ice-bringer
//

var DeathKnightStill = UnitStill;
var DeathKnightMove = ["unbreakable begin","frame 0", "move 3", "wait 2", "frame 5", "move 3", "wait 2",
    "frame 5", "move 4", "wait 2", "frame 10", "move 3", "wait 2",
    "frame 10", "move 3", "wait 2", "frame 15", "move 3", "wait 2",
    "frame 15", "move 4", "wait 2", "frame 20", "move 3", "wait 2",
    "frame 20", "move 3", "wait 2", "frame 0", "move 3", "unbreakable end", "wait 1"];
var DeathKnightDeath = ["unbreakable begin", "frame 45", "wait 5", "frame 50", "wait 5", "frame 55", "wait 5",
    "frame 60", "wait 5", "frame 60", "unbreakable end", "wait 1"];

animations["animations-death-knight"] = {
    Still : DeathKnightStill,
    Move : DeathKnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5",
        "frame 35", "attack", "sound death-knight-attack", "wait 7",
        "frame 40", "wait 5", "frame 0", "wait 17", "frame 0", "unbreakable end", "wait 1"],
    Death : DeathKnightDeath
};

animations["animations-evil-knight"] = {
    Still : DeathKnightStill,
    Move : DeathKnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5",
        "frame 35", "attack", "sound teron-gorefiend-attack", "wait 7",
        "frame 40", "wait 5", "frame 0", "wait 17", "frame 0", "unbreakable end", "wait 1"],
    Death : DeathKnightDeath
};

animations["animations-ice-bringer"] = {
    Still : DeathKnightStill,
    Move : DeathKnightMove,
    Attack : ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5",
        "frame 35", "attack", "sound gul-dan-attack", "wait 7",
        "frame 40", "wait 5", "frame 0", "wait 17", "frame 0", "unbreakable end", "wait 1"],
    Death : DeathKnightDeath
};


animations["animations-goblin-sappers"] = {
    Still : UnitStill,
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 1", "frame 10", "move 3", "wait 1",
        "frame 10", "move 2", "wait 1", "frame 25", "move 3", "wait 2",
        "frame 25", "move 3", "wait 1", "frame 40", "move 2", "wait 1",
        "frame 40", "move 3", "wait 1", "frame 55", "move 3", "wait 1",
        "frame 55", "move 2", "wait 1", "frame 65", "move 3", "wait 2",
        "frame 65", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 15", "wait 3",
        "frame 30", "attack", "sound goblin-sappers-attack", "wait 5",
        "frame 45", "wait 3", "frame 0", "wait 13", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 3", "frame 20", "wait 3", "frame 35", "wait 3",
        "frame 50", "wait 3", "frame 60", "wait 3", "frame 70", "wait 3",
        "frame 70", "unbreakable end", "wait 1"]
};


animations["animations-orc-oil-tanker"] = {
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


animations["animations-orc-transport"] = {
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


animations["animations-troll-destroyer"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "sound troll-destroyer-attack", "wait 119",
        "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


animations["animations-ogre-juggernaught"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 1", "frame 0", "move 2", "wait 2",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 1",
        "frame 0", "move 2", "wait 2", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "sound ogre-juggernaught-attack", "wait 127",
        "frame 0", "wait 102", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 50", "frame 10", "wait 50", "frame 10", "unbreakable end", "wait 1"]
};


//
    // dragon, fire-breeze
//

var DragonStill = ["frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
    "frame 15", "wait 6"];
var DragonMove = ["unbreakable begin", "frame 0", "wait 1", "frame 0", "move 3", "wait 1",
    "frame 0", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 5", "move 3", "wait 1", "frame 5", "move 3", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 10", "move 2", "wait 1",
    "frame 10", "move 3", "wait 1", "frame 15", "move 3", "wait 1",
    "frame 15", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"];
var DragonDeath = ["unbreakable begin", "frame 25", "wait 5", "frame 30", "wait 5", "frame 35", "wait 5",
    "frame 40", "wait 5", "frame 45", "wait 5", "frame 45", "unbreakable end", "wait 1"];

animations["animations-dragon"] = {
    Still : DragonStill,
    Move : DragonMove,
    Attack : ["unbreakable begin", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 15", "wait 1",
        "frame 20", "attack", "sound dragon-attack", "wait 20",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "unbreakable end", "wait 1"],
    Death : DragonDeath
};

animations["animations-fire-breeze"] = {
    Still : DragonStill,
    Move : DragonMove,
    Attack : ["unbreakable begin", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 15", "wait 1",
        "frame 20", "attack", "sound deathwing-attack", "wait 20",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "wait 6", "frame 5", "wait 6", "frame 10", "wait 6",
        "frame 15", "wait 6", "frame 0", "wait 6", "frame 5", "wait 6",
        "frame 10", "wait 6", "frame 15", "wait 6", "frame 0", "wait 6",
        "frame 5", "wait 6", "frame 10", "wait 6", "frame 15", "wait 6",
        "frame 0", "unbreakable end", "wait 1"],
    Death : DragonDeath
};

animations["animations-orc-submarine"] = {
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
        "frame 10", "attack", "sound giant-turtle-attack", "wait 25",
        "frame 5", "wait 25", "frame 0", "wait 29", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"]
};


animations["animations-goblin-zeppelin"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 4", "wait 1", "frame 0", "move 3", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 0", "move 3", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 0", "move 4", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 0", "move 3", "wait 1",
        "frame 0", "move 3", "wait 1", "frame 0", "move 3", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"]
};


animations["animations-eye-of-vision"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 8", "wait 1", "frame 0", "move 8", "wait 1",
        "frame 0", "move 8", "wait 1", "frame 0", "move 8", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "unbreakable end", "wait 1"]
};


animations["animations-skeleton"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 2", "frame 10", "move 3", "wait 2",
        "frame 10", "move 3", "wait 1", "frame 25", "move 2", "wait 2",
        "frame 25", "move 3", "wait 3", "frame 0", "move 2", "wait 1",
        "frame 0", "move 3", "wait 2", "frame 40", "move 3", "wait 2",
        "frame 40", "move 3", "wait 1", "frame 55", "move 2", "wait 2",
        "frame 55", "move 3", "wait 2", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 15", "wait 4", "frame 30", "wait 4",
        "frame 45", "attack", "sound skeleton-attack", "wait 4",
        "frame 60", "wait 4", "frame 0", "wait 18", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 3", "frame 20", "wait 3", "frame 35", "wait 3",
        "frame 50", "wait 3", "frame 65", "wait 3", "frame 65", "unbreakable end", "wait 1"]
};


animations["animations-orc-guard-tower"] = {
    Still : BuildingStill,
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 59",
        "frame 0", "unbreakable end", "wait 1"]
};

////////
    //	Cannon Tower, Cannon Tower
animations["animations-orc-cannon-tower"] = {
    Still : BuildingStill,
    Attack : ["unbreakable begin", "frame 0", "attack", "wait 150",
        "frame 0", "unbreakable end", "wait 1"]
};
