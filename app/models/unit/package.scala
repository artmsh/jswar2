package models

package object unit {
  val defaults: Array[(String, UnitCharacteristic)] = Array(
    ("unit-footman", new Swordsman),
    ("unit-grunt", new Swordsman),
    ("unit-peasant", new Worker),
    ("unit-peon", new Worker),
    ("unit-ballista", new AbstractCatapult),
    ("unit-catapult", new AbstractCatapult),
    ("unit-knight", new Knight),
    ("unit-ogre", new Knight),
    ("unit-archer", new Bowman),
    ("unit-axethrower", new Bowman),
    ("unit-mage", new Worker), // todo implement
    ("unit-death-knight", new Worker), // todo implement
    ("unit-paladin", new Worker), // todo implement
    ("unit-ogre-mage", new Worker), // todo implement
    ("unit-dwarves", new Worker), // todo implement
    ("unit-goblin-sappers", new Worker), // todo implement
    ("unit-attack-peasant", new Worker), // todo implement
    ("unit-attack-peon", new Worker), // todo implement
    ("unit-ranger", new Worker), // todo implement
    ("unit-berserker", new Worker), // todo implement
    ("unit-female-hero", new Worker), // todo implement
    ("unit-evil-knight", new Worker), // todo implement
    ("unit-flying-angel", new Worker), // todo implement
    ("unit-fad-man", new Worker), // todo implement
    ("unit-white-mage", new Worker), // todo implement
    ("unit-beast-cry", new Worker), // todo implement
    ("unit-human-oil-tanker", new Worker), // todo implement
    ("unit-orc-oil-tanker", new Worker), // todo implement
    ("unit-human-transport", new Worker), // todo implement
    ("unit-orc-transport", new Worker), // todo implement
    ("unit-human-destroyer", new Worker), // todo implement
    ("unit-orc-destroyer", new Worker), // todo implement
    ("unit-battleship", new Worker), // todo implement
    ("unit-ogre-juggernaught", new Worker), // todo implement
    ("", new Worker), // todo implement
    ("unit-fire-breeze", new Worker), // todo implement
    ("", new Worker), // todo implement
    ("", new Worker), // todo implement
    ("unit-human-submarine", new Worker), // todo implement
    ("unit-orc-submarine", new Worker), // todo implement
    ("unit-balloon", new Worker), // todo implement
    ("unit-zeppelin", new Worker), // todo implement
    ("unit-gryphon-rider", new Worker), // todo implement
    ("unit-dragon", new Worker), // todo implement
    ("unit-knight-rider", new Worker), // todo implement
    ("unit-eye-of-vision", new Worker), // todo implement
    ("unit-arthor-literios", new Worker), // todo implement
    ("unit-quick-blade", new Worker), // todo implement
    ("", new Worker), // todo implement
    ("unit-double-head", new Worker), // todo implement
    ("unit-wise-man", new Worker), // todo implement
    ("unit-ice-bringer", new Worker), // todo implement
    ("unit-man-of-light", new Worker), // todo implement
    ("unit-sharp-axe", new Worker), // todo implement
    ("", new Worker), // todo implement
    ("unit-skeleton", new Skeleton),
    ("unit-daemon", new Daemon),
    ("unit-critter", new Critter),
    ("unit-farm", new AbstractFarm),
    ("unit-pig-farm", new AbstractFarm),
    ("unit-human-barracks", new Barracks),
    ("unit-orc-barracks", new Barracks),
    ("unit-church", new Worker), // todo implement
    ("unit-altar-of-storms", new Worker), // todo implement
    ("unit-human-watch-tower", new WatchTower),
    ("unit-orc-watch-tower", new WatchTower),
    ("unit-stables", new Worker), // todo implement
    ("unit-ogre-mound", new Worker), // todo implement
    ("unit-inventor", new Worker), // todo implement
    ("unit-alchemist", new Worker), // todo implement
    ("unit-gryphon-aviary", new Worker), // todo implement
    ("unit-dragon-roost", new Worker), // todo implement
    ("unit-human-shipyard", new Worker), // todo implement
    ("unit-orc-shipyard", new Worker), // todo implement
    ("unit-town-hall", new Worker), // todo implement
    ("unit-great-hall", new Worker), // todo implement
    ("unit-elven-lumber-mill", new LumberMill),
    ("unit-troll-lumber-mill", new LumberMill),
    ("unit-human-foundry", new Worker), // todo implement
    ("unit-orc-foundry", new Worker), // todo implement
    ("unit-mage-tower", new Worker), // todo implement
    ("unit-temple-of-the-damned", new Worker), // todo implement
    ("unit-human-blacksmith", new Blacksmith),
    ("unit-orc-blacksmith", new Blacksmith),
    ("unit-human-refinery", new Worker), // todo implement
    ("unit-orc-refinery", new Worker), // todo implement
    ("unit-human-oil-platform", new Worker), // todo implement
    ("unit-orc-oil-platform", new Worker), // todo implement
    ("unit-keep", new Worker), // todo implement
    ("unit-stronghold", new Worker), // todo implement
    ("unit-castle", new Worker), // todo implement
    ("unit-fortress", new Worker), // todo implement
    ("unit-gold-mine", new GoldMine),
    ("unit-oil-patch", new OilPatch),
    ("unit-human-start-location", new Worker), // todo implement
    ("unit-orc-start-location", new Worker), // todo implement
    ("unit-human-guard-tower", new Worker), // todo implement
    ("unit-orc-guard-tower", new Worker), // todo implement
    ("unit-human-cannon-tower", new Worker), // todo implement
    ("unit-orc-cannon-tower", new Worker), // todo implement
    ("unit-circle-of-power", new CircleOfPower),
    ("unit-dark-portal", new DarkPortal),
    ("unit-runestone", new Runestone),
    ("unit-human-wall", new Worker), // todo implement
    ("unit-orc-wall", new Worker) // todo
  )
}
