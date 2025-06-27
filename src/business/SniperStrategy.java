package business;

import business.entities.Member;

/**
 * Sniper combat strategy implementation.
 * This strategy always chooses to attack, representing a sniper's aggressive approach.
 */
public class SniperStrategy extends CombatStrategy {

    /**
     * Constructs a SniperStrategy with the given name.
     *
     * @param strategyName The name identifying the strategy
     */
    public SniperStrategy(String strategyName) {
        super(strategyName);
    }

    /**
     * Decides the combat action based on sniper strategy logic.
     * Always chooses to attack, representing the sniper's aggressive nature.
     *
     * @param attacker The member whose turn it is to perform
     * @return The chosen combat action (always ATTACK)
     */
    @Override
    public CombatAction decideAction(Member attacker) {
        return CombatAction.ATTACK;
    }
}
