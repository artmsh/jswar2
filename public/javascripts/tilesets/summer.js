tilesets["Summer"] = {
    name: "summer",
    image: "assets/images/tilesets/summer/terrain/summer.png",
    table: [
        {slot: "solid", flags: ["unused"], table: []},                                         // 000-010
        {slot: "solid", flags: ["light-water", "water"], table: [328, 329, 329, 330]},         // 010-020
        {slot: "solid", flags: ["dark-water", "water"], table: [331, 332, 332, 333]},          // 020-030
        {slot: "solid", flags: ["light-coast", "land", "no-building"],                         // 030-040
            table: [334, 335, 336,   0, 337, 338, 339, 340, 341, 342, 343, 344]},
        {slot: "solid", flags: ["dark-coast", "land", "no-building"],                          // 040-050
            table: [345, 346, 347,   0, 348, 349, 350, 351, 352, 353, 354, 355]},
        {slot: "solid", flags: ["light-grass", "land"],                                        // 050-060
            table: [356, 357, 356,   0, 358, 359, 360, 361, 362, 363, 358, 359, 358, 359, 358, 359]},
        {slot: "solid", flags: ["dark-grass", "land"],                                         // 060-070
            table: [364, 365, 364,   0, 366, 367, 368, 369, 370, 371, 366, 367, 366, 367, 366, 367]},
        {slot: "solid", flags: ["forest", "land", "forest", "unpassable"],                     // 070-080
            table: [125, 127, 128]},
        {slot: "solid", flags: ["rocks", "land", "rock", "unpassable"],                        // 080-090
            table: [165, 177, 178, 179]},
        {slot: "solid", flags: ["human-closed-wall", "land", "human", "wall", "unpassable"],   // 090-0A0
            table: [16,   0,  52,   0,  88]},
        {slot: "solid", flags: ["orc-closed-wall", "land", "wall", "unpassable"],              // 0A0-0B0
            table:[34,   0,  70,   0,  88]}
    ]
};