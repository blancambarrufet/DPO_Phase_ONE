package business;

/**
 * Factory class for creating combat strategy instances.
 * Provides methods to create strategies by name or by type.
 */
public class StrategyFactory {

    /**
     * Creates a combat strategy based on the provided strategy name.
     *
     * @param name The name of the strategy (case-insensitive)
     * @return A new CombatStrategy instance
     * @throws IllegalArgumentException if the strategy name is invalid
     */
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

    /**
     * Creates a combat strategy based on the provided strategy type.
     *
     * @param type The StrategyType enum value
     * @return A new CombatStrategy instance
     */
    public static CombatStrategy createStrategyByType(StrategyType type) {
        return switch (type) {
            case BALANCED  -> new BalancedStrategy("balanced");
            case OFFENSIVE -> new OffensiveStrategy("offensive");
            case DEFENSIVE -> new DefensiveStrategy("defensive");
            case SNIPER    -> new SniperStrategy("sniper");
        };
    }
}
