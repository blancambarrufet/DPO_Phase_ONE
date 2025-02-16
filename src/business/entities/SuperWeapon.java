package business.entities;

public class SuperWeapon extends Weapon {

    public SuperWeapon(long id, String name, int power, int durability) {
        super(id, name, power, durability);
    }

    @Override
    public int getAttackPower() {
        return getPower(); // Weight factor applied later during combat calculations
    }

    public int computeWeaponAttack(int attackerWeight) {
        return getPower() * attackerWeight;
    }
}
