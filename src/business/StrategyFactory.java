package business;

public class StrategyFactory {

    public static CombatStrategy createStrategyByName(String name) {
        StrategyType type = switch (name.toLowerCase()) {
            case "balanced"  -> StrategyType.BALANCED;
            case "offensive" -> StrategyType.OFFENSIVE;
            case "defensive" -> StrategyType.DEFENSIVE;
            case "sniper"    -> StrategyType.SNIPER;
            default -> throw new IllegalArgumentException("Invalid strategy: " + name);
        };

        return createStrategyByType(type);
    }

    public static CombatStrategy createStrategyByType(StrategyType type) {
        return switch (type) {
            case BALANCED  -> new BalancedStrategy("balanced");
            case OFFENSIVE -> new OffensiveStrategy("offensive");
            case DEFENSIVE -> new DefensiveStrategy("defensive");
            case SNIPER    -> new SniperStrategy("sniper");
        };
    }
}
