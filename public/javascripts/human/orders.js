var Orders = {
    "still" : function (action, state) {
        this.action = action;
        this.state = state;
    },
    "move" : function (action, range, pos) {
        this.action = action;
        this.range = range;
        this.pos = pos;
    }
};

with(Orders.still) {
    Orders.still.STANDBY = 0;
    Orders.still.ATTACK = 1;

    // Copypasted from stratagus cpp code
    prototype.execute = function(unit) {
        // If unit is not bunkered and removed, wait
        if (unit.Removed && (unit.Container == undefined || !unit.Container.type.AttackFromTransporter)) {
            return;
        }

        this.finished = false;

        switch (this.state) {
            case Orders.still.STANDBY : unit.updateAnimation(animations[unit.type.Animations].Still); break;
            case Orders.still.ATTACK : /*  */
        }

        if (unit.Animation.Unbreakable) {
            return;
        }

        this.state = Orders.still.STANDBY;
        this.finished = this.action == UnitAction.Still;

//        if (this.Action == UnitAction.StandGround || unit.Removed || unit.CanMove() == false) {
//            if (unit.AutoCastSpell) {
//                this->AutoCastStand(unit);
//            }
//            if (unit.IsAgressive()) {
//                this->AutoAttackStand(unit);
//            }
//        } else {
//            if (AutoCast(unit) || (unit.IsAgressive() && AutoAttack(unit))
//                || AutoRepair(unit)
//                || MoveRandomly(unit)) {
//            }
//        }
    };
}

with(Orders.move) {
    prototype.execute = function(unit) {
        if (unit.Wait > 0) {
            unit.Wait--;
            return;
        }


    }
}