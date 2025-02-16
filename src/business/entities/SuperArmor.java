package business.entities;

public class SuperArmor extends Armor {

    public SuperArmor(long id, String name, int power, int durability) {
        super(id, name, power, durability);
    }

    @Override
    public int getDefenseValue() {
        // Super armor multiplies its power by the defender's weight
        return getPower(); // Weight factor applied later during combat calculations
    }

    public int computeArmorValue(int defenderWeight) {
        return getPower() * defenderWeight;
    }
}
