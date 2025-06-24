package business;

import business.entities.Member;

public class BalancedStrategy extends CombatStrategy {

    public BalancedStrategy(String strategyName) {
        super(strategyName);
    }

    @Override
    public CombatAction decideAction(Member attacker) {
        if (attacker.getWeapon() == null) {
            return CombatAction.REQUEST_WEAPON;
        } else {
            if (attacker.getArmor() != null) {
                if (attacker.getDamageTaken() >= 0.5 && attacker.getDamageTaken() <= 1.0) {
                    return CombatAction.DEFEND;
                } else {
                    return CombatAction.ATTACK;
                }
            } else {
                return CombatAction.ATTACK;
            }
        }
    }
}
