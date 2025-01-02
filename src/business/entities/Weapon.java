package business.entities;

public class Weapon extends Item {
    private Weapon(String name, int id, int power, int durability, String type) {
        super(name, id, power, durability, type);
    }
    private int defenseValue;

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }
}
