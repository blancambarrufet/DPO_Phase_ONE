package business.entities;

public class Armor extends Item {

    public Armor(long id, String name, int power, int durability) {
        super(name, id, power, durability);
    }

    public int getDefenseValue() {
        return getPower();
    }

}
