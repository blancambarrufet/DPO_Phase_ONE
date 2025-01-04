package business.entities;

public class Weapon extends Item {

    public Weapon(long id, String name, int power, int durability) {
        super(name, id, power, durability);
    }

    public int getAttackPower() {
        return getPower();
    }

    public void setAttackValue(int attackValue) {
        setPower(attackValue);;
    }
}
