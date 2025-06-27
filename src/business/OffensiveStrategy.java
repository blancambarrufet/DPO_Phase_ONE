package business;

import business.entities.Member;

/**
 * Offensive combat strategy implementation.
 * This strategy prioritizes attack actions, requesting a weapon if none is available.
 */
public class OffensiveStrategy extends CombatStrategy {

    /**
     * Constructs an OffensiveStrategy with the given name.
     *
     * @param strategyName The name identifying the strategy
     */
    public OffensiveStrategy(String strategyName) {
        super(strategyName);
    }

    /**
     * Decides the combat action based on offensive strategy logic.
     * If the member doesn't have a weapon, requests one. Otherwise, attacks.
     *
     * @param attacker The member whose turn it is to perform
     * @return The chosen combat action (REQUEST_WEAPON or ATTACK)
     */
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
