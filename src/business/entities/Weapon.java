package business.entities;

public class Weapon extends Item {

    public Weapon(long id, String name, int power, int durability) {
        super(name, id, power, durability, "Weapon");
    }

    @Override
    public int getAttackPower() {
        return getPower(); // Normal weapon uses its base power for attack
    }

    @Override
    public int getDefenseValue() {
        return 0; // Weapons do not provide defense
    }
}
