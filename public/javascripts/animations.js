var UnitStill = ["frame 0", "wait 4", "random-goto 99 no-rotate", "random-rotate 1", "label no-rotate", "wait 1"/*,*/];
var BuildingStill = ["frame 0", "wait 4", "frame 0", "wait 1"];

animations["animations-daemon"] = {
    Still : ["frame 0", "wait 4", "frame 5", "wait 4", "frame 10", "wait 4",
    "frame 15", "wait 4"],
    Move : ["unbreakable begin", "frame 0", "move 3", "wait 1", "frame 0", "move 3", "wait 1",
        "frame 5", "move 2", "wait 1", "frame 5", "move 3", "wait 1",
        "frame 10", "move 2", "wait 1", "frame 10", "move 3", "wait 1",
        "frame 10", "move 3", "wait 1", "frame 15", "move 2", "wait 1",
        "frame 15", "move 3", "wait 1", "frame 20", "move 3", "wait 1",
        "frame 20", "move 3", "wait 1", "frame 0", "move 2", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "wait 4", "frame 5", "wait 4",
        "frame 10", "wait 4", "frame 15", "wait 4", "frame 20", "wait 4",
        "frame 20", "wait 1", "frame 25", "wait 4", "frame 30", "wait 4",
        "frame 35", "wait 4", "frame 40", "attack", "wait 4",
        "frame 45", "wait 4", "frame 0", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 50", "wait 5", "frame 55", "wait 5", "frame 60", "wait 5",
        "frame 65", "wait 5", "frame 65", "unbreakable end", "wait 1"]
};

animations["animations-critter"] = {
    Still : ["frame 0", "wait 4", "frame 0", "wait 1"],
    Move : ["unbreakable begin", "frame 0", "move 2", "wait 2", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "move 2", "wait 3", "frame 0", "move 2", "wait 3",
        "frame 0", "unbreakable end", "wait 1"],
    Attack : ["unbreakable begin", "frame 0", "attack", "unbreakable end", "wait 1"],
    Death : ["unbreakable begin", "frame 5", "wait 200", "frame 5", "unbreakable end", "wait 1"]
};


animations["animations-building"] = {
    Still : BuildingStill,
    Research : BuildingStill,
    Train : BuildingStill,
    Upgrade : ["frame 1", "wait 4", "frame 1", "wait 1"]
};

animations["animations-oil-platform"] = {
    Still : ["label start", "if-var v.ResourceActive.Value >= 1 active",
    "frame 0", "wait 4", "frame 0", "wait 1", "goto start",
    "label active", "frame 2", "wait 4", "frame 2", "wait 1"]
};

animations["animations-gold-mine"] = {
    Still : ["label start", "if-var v.ResourceActive.Value >= 1 active",
    "frame 0", "wait 4", "frame 0", "wait 1", "goto start",
    "label active", "frame 1", "wait 4", "frame 1", "wait 1"]
};


animations["animations-human-dead-body"] = {
    Death : ["unbreakable begin", "frame 0", "wait 200", "frame 10", "wait 200", "frame 15", "wait 200",
    "frame 20", "wait 200", "frame 25", "wait 200", "frame 25", "unbreakable end", "wait 1"]
};

animations["animations-orc-dead-body"] = {
    Death : ["unbreakable begin", "frame 5", "wait 200", "frame 10", "wait 200", "frame 15", "wait 200",
    "frame 20", "wait 200", "frame 25", "wait 200", "frame 25", "unbreakable end", "wait 1"]
};

animations["animations-dead-sea-body"] = {
    Death : ["unbreakable begin", "frame 30", "wait 100", "frame 30", "wait 100",
    "frame 30", "unbreakable end", "wait 1"]
};

animations["animations-destroyed-place"] = {
    Death : ["unbreakable begin", "frame 0", "wait 200", "frame 1", "wait 200", "frame 1", "unbreakable end", "wait 1"]
};

animations["animations-destroyed-place-water"] = {
    Death : ["unbreakable begin", "frame 2", "wait 200", "frame 3", "wait 200", "frame 1", "unbreakable end", "wait 1"]
};