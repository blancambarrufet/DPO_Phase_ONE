package business;

import business.entities.Member;

public class SniperStrategy extends CombatStrategy {

    public SniperStrategy(String strategyName) {
        super(strategyName);
    }

    @Override
    public CombatAction decideAction(Member attacker) {
        return CombatAction.ATTACK;
    }
}
