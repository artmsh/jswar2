function Player(type, race, aiType, startRes, color) {
    this.type = PlayerType[type];
    switch (race) {
        case 0: this.race = Race.HUMAN; break;
        case 1: this.race = Race.ORC; break;
        case 2: this.race = "Neutral";
    }

    this.aiType = aiType;
    this.res = startRes;
    this.color = color;
}