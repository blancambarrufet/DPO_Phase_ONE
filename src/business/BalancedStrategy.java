package business;

import business.entities.Member;

/**
 * balance strategy class child for CombatStrategy
 * This strategy balances between attacking, defending, and requesting a new weapon.
 */
public class BalancedStrategy extends CombatStrategy {

    /**
     * Constructs a BalancedStrategy with the given strategy name.
     *
     * @param strategyName The name of the strategy, typically "balanced".
     */
    public BalancedStrategy(String strategyName) {
        super(strategyName);
    }

    /**
     * Decides the combat action of the given member based on their weapon, armor, and damage taken.
     *
     * @param attacker The member taking the turn.
     * @return The decided action, which may be ATTACK, DEFEND, or REQUEST_WEAPON.
     */
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
