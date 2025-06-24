package business;

public class StrategyFactory {

    public static CombatStrategy getStrategy(String name) {
        StrategyType type = switch (name.toLowerCase()) {
            case "balanced"  -> StrategyType.BALANCED;
            case "offensive" -> StrategyType.OFFENSIVE;
            case "defensive" -> StrategyType.DEFENSIVE;
            case "sniper"    -> StrategyType.SNIPER;
            default -> throw new IllegalArgumentException("Invalid strategy: " + name);
        };

        return fromType(type);
    }

    public static CombatStrategy fromType(StrategyType type) {
        return switch (type) {
            case BALANCED  -> new BalancedStrategy("balanced");
            case OFFENSIVE -> new OffensiveStrategy("offensive");
            case DEFENSIVE -> new DefensiveStrategy("defensive");
            case SNIPER    -> new SniperStrategy("sniper");
        };
    }
}
