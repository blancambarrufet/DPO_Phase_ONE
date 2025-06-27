package business;

import business.entities.Member;

/**
 * Defensive combat strategy implementation.
 * This strategy prioritizes defense when the member has armor and low damage taken,
 * otherwise defaults to attack.
 */
public class DefensiveStrategy extends CombatStrategy {

    /**
     * Constructs a DefensiveStrategy with the given name.
     *
     * @param strategyName The name identifying the strategy
     */
    public DefensiveStrategy(String strategyName) {
        super(strategyName);
    }

    /**
     * Decides the combat action based on defensive strategy logic.
     * If the member has armor and has taken less than 1.0 damage, chooses to defend.
     * Otherwise, chooses to attack.
     *
     * @param attacker The member whose turn it is to perform
     * @return The chosen combat action (DEFEND or ATTACK)
     */
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
