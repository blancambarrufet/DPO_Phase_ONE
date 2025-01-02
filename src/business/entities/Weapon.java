package business.entities;

public class Weapon extends Item {

    private int attackValue;

    public Weapon(String name, int id, int power, int durability, int attackValue) {
        super(name, id, power, durability);
        this.attackValue = attackValue;
    }

    public int getAttackPower() {
        return attackValue;
    }

    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }
}
