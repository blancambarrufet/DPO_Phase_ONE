package business;

import business.entities.Member;

public class DefensiveStrategy extends CombatStrategy {

    public DefensiveStrategy(String strategyName) {
        super(strategyName);
    }

    @Override
    public CombatAction decideAction(Member attacker) {
        if (attacker.getArmor() != null) {
            if (attacker.getDamageTaken() < 1.0) {
                return CombatAction.DEFEND;
            } else {
                return CombatAction.ATTACK;
            }
        } else {
            return CombatAction.ATTACK;
        }
    }
}
