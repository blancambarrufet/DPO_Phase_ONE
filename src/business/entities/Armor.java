package business.entities;

public class Armor extends Item {

    public Armor(long id, String name, int power, int durability) {
        super(name, id, power, durability, "Armor");
    }

    @Override
    public int getDefenseValue() {
        return getPower(); // Normal armor just uses its power for defense
    }

    @Override
    public int getAttackPower() {
        return 0; // Armor does not contribute to attack
    }
}
