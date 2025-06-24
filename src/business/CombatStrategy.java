package business;

import business.entities.Member;

public abstract class CombatStrategy {

    private String strategyName;

    public CombatStrategy(String strategyName) {
        this.strategyName = strategyName;
    }

    public String getStrategyName() {
        return strategyName;
    }

    public abstract CombatAction decideAction(Member attacker);

}
