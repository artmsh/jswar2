var fonts = {
    game : "14px LifeCraft",
    small : "7px monospace"
};

var info_panel_x = 0;
var info_panel_y = 160;

//var min_damage = ActiveUnitVar("PiercingDamage") - 2;
//var max_damage = Add(ActiveUnitVar("PiercingDamage"), ActiveUnitVar("BasicDamage"));
//var damage_bonus = Sub(ActiveUnitVar("PiercingDamage", "Value", "Type"),
//    ActiveUnitVar("PiercingDamage", "Value", "Initial"));

var UI = {
    CompletedBarColorRGB : 'rgb(48, 100, 4)',
    CompletedBarShadow : true
};

var infoPanelContent = [
    // Default presentation. ////////////////////////
    {
        Ident : "panel/general/contents",
        Pos : [info_panel_x, info_panel_y], DefaultFont : "game",
        Contents : [
            {
                Pos : [5, 51], Condition : {ShowOpponent : false, HideNeutral : true},
                More : ["LifeBar", {Variable1 : "HitPoints", Variable2 : "MaxHitPoints", Height : 7, Width : 45}]
            },
            {
                Pos : [31, 64], Condition : {ShowOpponent : false, HideNeutral : true},
                More : ["FormattedText2", { Font : "small", Variable1 : "HitPoints", Variable2 : "MaxHitPoints",
                    Format : "%d/%d", Centered : true}]
            },

            { Pos : [114, 37], More : ["Text", {Text : function(unit) { return unit.type.Name; }, Centered : true}] },
//            { Pos : [114, 25], More : ["Text", {Text : Line(2, UnitName("Active"), 110, "game"), Centered : true}] },

            // Ressource Left
            { Pos : [88, 86], Condition : {ShowOpponent : false, GivesResource : "only"},
                More : ["FormattedText2", {Format : "%s Left:%d", Variable1 : "GivesResource",
                    Variable2 : "ResourceLeft", Centered : true}]
            },

            // Construction
            { Pos : [12, 153], Condition : {ShowOpponent : false, HideNeutral : true, Build : "only"},
                More : ["CompleteBar", {Variable1 : "Build", Variable2 : "BuildTime", Width : 152, Height : 18}]
            },
            { Pos : [50, 156], Condition : {ShowOpponent : false, HideNeutral : true, Build : "only"},
                More : ["Text", "% Complete"]},
            { Pos : [107, 78], Condition : {ShowOpponent : false, HideNeutral : true, Build : "only"},
                More : ["Icon", {Unit : "Worker"}]}


        ] },
// Supply Building constructed.////////////////
    {
        Ident : "panel/building/contents",
        Pos : [info_panel_x, info_panel_y], DefaultFont : "game",
        Condition : {ShowOpponent : false, HideNeutral : true, Build : "false", Supply : "only", Training : "false", UpgradeTo : "false"},
// FIXME more condition. not town hall.
        Contents : [
// Food building
            { Pos : [16, 71], More : ["Text", "Usage"] },
            // todo fix
            { Pos : [58, 86], More : ["Text", {Text : "Supply : ", Variable : "Supply", Component : "Max"}] },
            { Pos : [51, 102], More : [ "Text", {Text : function(unit) {
                var demand = unit.Demand.Max;
                if (unit.Demand.Max > unit.Supply.Max) {
                    demand = "~<" + demand + "~>";
                }

                return "Demand : " + demand;
            }}]
            }

        ] },
// All own unit /////////////////
    {
        Ident : "panel/all/unit/contents",
        Pos : [info_panel_x, info_panel_y],
        DefaultFont : "game",
        Condition : {ShowOpponent : false, HideNeutral : true, Build : "false"},
        Contents : [
            { Pos : [102, 37 + 15 + 32 + 15], Condition : {PiercingDamage : "only"},
                More : ["Text", {Text : function(unit) {
                    var dmg = (unit.type.BasicDamage - 2) + '\u2013' + (unit.vars.PiercingDamage + unit.type.BasicDamage);

                    var bonusDmg = unit.vars.PiercingDamage - unit.type.PiercingDamage;
                    if (bonusDmg > 0) {
                        dmg += "~<+" + bonusDmg + "~>";
                    }

                    return dmg;
                }}]
            },
            {
                Pos : [102, 37 + 15 + 32 + 15], Condition : {PiercingDamage : "only"},
                More : ["Text", {Text : "Damage: ", TextAlign : "right"}]
            },

            { Pos : [102, 37 + 15 + 32 + 15 * 2], Condition : {MaxAttackRange : "only"},
                More : ["Text", { Text : "Range: ", TextAlign : "right"}]
            },
            { Pos : [102, 37 + 15 + 32 + 15 * 2], Condition : {MaxAttackRange : "only"},
                More : ["Text", {Variable : "MaxAttackRange" , Stat : true}]
            },

// Research
            { Pos : [12, 153], Condition : {Research : "only"},
                More : ["CompleteBar", {Variable1 : "Research", Variable2 : "ResearchTime", Width : 152, Height : 18}]
            },
            { Pos : [16, 86], Condition : {Research : "only"}, More : ["Text", "Researching:"]},
            { Pos : [50, 156], Condition : {Research : "only"}, More : ["Text", "% Complete"]},
// Training
            { Pos : [12, 153], Condition : {Training : "only"},
                More : ["CompleteBar", {Variable1 : "Training", Variable2 : "TrainingTime", Width : 152, Height : 18}]
            },
            { Pos : [50, 156], Condition : {Training : "only"}, More : ["Text", "% Complete"]},
// Upgrading To
            { Pos : [12, 153], Condition : {UpgradeTo : "only"},
                More : ["CompleteBar", {Variable1 : "UpgradeTo", Variable2 : "UpgradeToTime", Width : 152, Height : 18}]
            },
            { Pos : [37,  86], More : ["Text", "Upgrading:"], Condition : {UpgradeTo : "only"} },
            { Pos : [50, 156], More : ["Text", "% Complete"], Condition : {UpgradeTo : "only"} },
// Mana
            { Pos : [16, 148], Condition : {Mana : "only"},
                More : ["CompleteBar", {Variable1 : "Mana", Variable2 : "MaxMana", Height : 16, Width : 140, Border : true}]
            },
            { Pos : [86, 150], More : ["Text", {Variable : "Mana"}], Condition : {Mana : "only"} }
//            ,
// Resource Carry
//            { Pos : [61, 149], Condition : {CarryResource : "only"},
//                More : ["FormattedText2", {Format : "Carry: %d %s", Variable : "CarryResource",
//                    Component1 : "Value", Component2 : "Name"}]
//            }

        ]
    },
// Attack Unit /////////////////////////////
    {
        Ident : "panel/attack/unit/contents",
        Pos : [info_panel_x, info_panel_y],
        DefaultFont : "game",
        Condition : {ShowOpponent : true, HideNeutral : true, Building : "false", Build : "false"},
        Contents : [
            { Pos : [114, 52],
                More : ["FormattedText", {Variable : "Level", Format : "Level %d", Centered: "true"}]
            },
            { Pos : [102, 37 + 15 + 32], Condition : {Armor : "only"},
                More : ["Text", { Text : "Armor: ", TextAlign : "right"}]
            },
            { Pos : [102, 37 + 15 + 32], Condition : {Armor : "only"},
                More : ["Text", { Variable : "Armor", Stat : true}]
            },
            { Pos : [102, 37 + 15 + 32 + 15 * 3], Condition : {SightRange : "only"},
                More : ["Text", {Text : "Sight: ", TextAlign : "right"}]
            },
            { Pos : [102, 37 + 15 + 32 + 15 * 3], Condition : {SightRange : "only"},
                More : ["Text", {Variable : "SightRange", Stat : true}]
            },
            { Pos : [102, 37 + 15 + 32 + 15 * 4], Condition : {Speed : "only"},
                More : ["Text", {Text : "Speed: ", TextAlign : "right"}]
            },
            { Pos : [102, 37 + 15 + 32 + 15 * 4], Condition : {Speed : "only"},
                More : ["Text", {Variable : "Speed", Stat : true}]
            }
        ]
    }
];
