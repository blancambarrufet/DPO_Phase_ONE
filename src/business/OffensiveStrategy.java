package business;

import business.entities.Member;

public class OffensiveStrategy extends CombatStrategy {

    public OffensiveStrategy(String strategyName) {
        super(strategyName);
    }

    @Override
    public CombatAction decideAction(Member attacker) {
        //If attacker does not have a weapon, request it
        if (attacker.getWeapon() == null ) {
            return CombatAction.REQUEST_WEAPON;
        }
        //else attack
        return CombatAction.ATTACK;
    }

}
