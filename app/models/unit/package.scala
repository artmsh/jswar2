package models

import format.pud._

package object unit {
  val swordsman = UnitCharacteristic.buildLandMelee(60, 2, 60, 50, 10, 6, 3, (31, 31), Some(attack), 60, 600, 0)

  val worker = UnitCharacteristic.buildLandMelee(30, 0, 50, 30, 10, 3, 2, (31, 31), Some(harvest), 45, 400, 0)

  val catapult = new UnitCharacteristic(new PudUnitCharacteristic(new BasicParams(9, 110, 0, (1, 1), 70, 100, true),
    new AttackParams(80, 0, true, new CanTarget(3), 8),
    new UiParams(true, (63, 63), catapult_rock, Some(attack)),
    new BuildParams(250, 900, 300, 0)),
    5)

  val knight = UnitCharacteristic.buildLandMelee(90, 4, 63, 100, 13, 8, 4, (42, 42), Some(attack), 90, 800, 100)

  val bowman = new UnitCharacteristic(new PudUnitCharacteristic(new BasicParams(5, 40, 0, (1, 1), 55, 60, true),
    new AttackParams(3, 6, true, new CanTarget(7), 4),
    new UiParams(true, (33, 33), arrow, Some(attack)),
    new BuildParams(70, 500, 50, 0)),
    10)

  val farm = UnitCharacteristic.buildBuilding(2, 400, (2, 2), 20, 100, (63, 63),
    new BuildParams(100, 500, 250, 0))

  val barracks = UnitCharacteristic.buildBuilding(1, 800, (3, 3), 30, 160, (95, 95),
    new BuildParams(200, 700, 450, 0))

  val lumberMill = UnitCharacteristic.buildBuilding(1, 600, (3, 3), 25, 150, (95, 95),
    new BuildParams(150, 600, 450, 0))

  val blacksmith = UnitCharacteristic.buildBuilding(1, 775, (3, 3), 15, 170, (95, 95),
    new BuildParams(200, 800, 450, 100))

  val watchTower = UnitCharacteristic.buildBuilding(9, 100, (2, 2), 55, 95, (63, 63),
    new BuildParams(60, 550, 200, 0))

  val critter = new UnitCharacteristic(new PudUnitCharacteristic(new BasicParams(2, 5, 0, (1, 1), 37, 1, false),
    AttackParams.none, new UiParams(true, (1, 1), none, Some(move)), BuildParams.none), 3)

  val defaults: Pud#UnitTypes = Vector(
    ("unit-footman", swordsman),
    ("unit-grunt", swordsman),
    ("unit-peasant", worker),
    ("unit-peon", worker),
    ("unit-ballista", catapult),
    ("unit-catapult", catapult),
    ("unit-knight", knight),
    ("unit-ogre", knight),
    ("unit-archer", bowman),
    ("unit-axethrower", bowman),
    ("unit-mage", worker), // todo implement
    ("unit-death-knight", worker), // todo implement
    ("unit-paladin", worker), // todo implement
    ("unit-ogre-mage", worker), // todo implement
    ("unit-dwarves", worker), // todo implement
    ("unit-goblin-sappers", worker), // todo implement
    ("unit-attack-peasant", worker), // todo implement
    ("unit-attack-peon", worker), // todo implement
    ("unit-ranger", worker), // todo implement
    ("unit-berserker", worker), // todo implement
    ("unit-female-hero", worker), // todo implement
    ("unit-evil-knight", worker), // todo implement
    ("unit-flying-angel", worker), // todo implement
    ("unit-fad-man", worker), // todo implement
    ("unit-white-mage", worker), // todo implement
    ("unit-beast-cry", worker), // todo implement
    ("unit-human-oil-tanker", worker), // todo implement
    ("unit-orc-oil-tanker", worker), // todo implement
    ("unit-human-transport", worker), // todo implement
    ("unit-orc-transport", worker), // todo implement
    ("unit-human-destroyer", worker), // todo implement
    ("unit-orc-destroyer", worker), // todo implement
    ("unit-battleship", worker), // todo implement
    ("unit-ogre-juggernaught", worker), // todo implement
    ("", worker), // todo implement
    ("unit-fire-breeze", worker), // todo implement
    ("", worker), // todo implement
    ("", worker), // todo implement
    ("unit-human-submarine", worker), // todo implement
    ("unit-orc-submarine", worker), // todo implement
    ("unit-balloon", worker), // todo implement
    ("unit-zeppelin", worker), // todo implement
    ("unit-gryphon-rider", worker), // todo implement
    ("unit-dragon", worker), // todo implement
    ("unit-knight-rider", worker), // todo implement
    ("unit-eye-of-vision", worker), // todo implement
    ("unit-arthor-literios", worker), // todo implement
    ("unit-quick-blade", worker), // todo implement
    ("", worker), // todo implement
    ("unit-double-head", worker), // todo implement
    ("unit-wise-man", worker), // todo implement
    ("unit-ice-bringer", worker), // todo implement
    ("unit-man-of-light", worker), // todo implement
    ("unit-sharp-axe", worker), // todo implement
    ("", worker), // todo implement
    ("unit-skeleton", worker), // new Skeleton), // todo implement
    ("unit-daemon", worker), // Daemon), // todo implement
    ("unit-critter", critter),
    ("unit-farm", farm),
    ("unit-pig-farm", farm),
    ("unit-human-barracks", barracks),
    ("unit-orc-barracks", barracks),
    ("unit-church", worker), // todo implement
    ("unit-altar-of-storms", worker), // todo implement
    ("unit-human-watch-tower", watchTower),
    ("unit-orc-watch-tower", watchTower),
    ("unit-stables", worker), // todo implement
    ("unit-ogre-mound", worker), // todo implement
    ("unit-inventor", worker), // todo implement
    ("unit-alchemist", worker), // todo implement
    ("unit-gryphon-aviary", worker), // todo implement
    ("unit-dragon-roost", worker), // todo implement
    ("unit-human-shipyard", worker), // todo implement
    ("unit-orc-shipyard", worker), // todo implement
    ("unit-town-hall", worker), // todo implement
    ("unit-great-hall", worker), // todo implement
    ("unit-elven-lumber-mill", lumberMill),
    ("unit-troll-lumber-mill", lumberMill),
    ("unit-human-foundry", worker), // todo implement
    ("unit-orc-foundry", worker), // todo implement
    ("unit-mage-tower", worker), // todo implement
    ("unit-temple-of-the-damned", worker), // todo implement
    ("unit-human-blacksmith", blacksmith),
    ("unit-orc-blacksmith", blacksmith),
    ("unit-human-refinery", worker), // todo implement
    ("unit-orc-refinery", worker), // todo implement
    ("unit-human-oil-platform", worker), // todo implement
    ("unit-orc-oil-platform", worker), // todo implement
    ("unit-keep", worker), // todo implement
    ("unit-stronghold", worker), // todo implement
    ("unit-castle", worker), // todo implement
    ("unit-fortress", worker), // todo implement
    ("unit-gold-mine", worker), // GoldMine),
    ("unit-oil-patch", worker), // OilPatch),
    ("unit-human-start-location", worker), // todo implement
    ("unit-orc-start-location", worker), // todo implement
    ("unit-human-guard-tower", worker), // todo implement
    ("unit-orc-guard-tower", worker), // todo implement
    ("unit-human-cannon-tower", worker), // todo implement
    ("unit-orc-cannon-tower", worker), // todo implement
    ("unit-circle-of-power", worker), //CircleOfPower),
    ("unit-dark-portal", worker), // DarkPortal),
    ("unit-runestone", worker), // Runestone),
    ("unit-human-wall", worker), // todo implement
    ("unit-orc-wall", worker) // todo
  )
}
