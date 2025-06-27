package business;

import business.entities.Member;
/**
 * Abstract base class representing a combat strategy used by a character during a battle.
 * A combat strategy defines how a member decides its action in each round of combat,
 * based on its current state.
 * Subclasses must implement the decision logic based on the strategy.
 */
public abstract class CombatStrategy {
    //The name of the strategy
    private final String strategyName;

    /**
     * Constructs a CombatStrategy with the given name.
     *
     * @param strategyName The name identifying the strategy.
     */
    public CombatStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    /**
     * Returns the name of the strategy.
     *
     * @return The strategy name.
     */
    public String getStrategyName() {
        return strategyName;
    }

    /**
     * Decides the combat action to be taken by a given member based on the strategy's logic.
     *
     * @param attacker The member whose turn it is to perform
     */
    public abstract CombatAction decideAction(Member attacker);

}
