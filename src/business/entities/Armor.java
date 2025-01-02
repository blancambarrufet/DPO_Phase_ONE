package business.entities;

public class Armor extends Item {

    private int defenseValue;

    public Armor(String name, int id, int power, int durability, int defenseValue) {
        super(name, id, power, durability);
        this.defenseValue = defenseValue;
    }

    public int getDefenseValue() {
        return defenseValue;
    }

    public void setDefenseValue(int defenseValue) {
        this.defenseValue = defenseValue;
    }
}
