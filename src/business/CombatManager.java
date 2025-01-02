package business;

import business.entities.Character;

public class CombatManager {

    // Attack action
    public void executeAttack(Character attacker, Character defender) {
        // Calculate attack damage
        double attackDamage = attacker.calculateAttack();

        // Calculate final damage for defender
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        // Apply damage to defender
        defender.takeDamage(finalDamage);

        // Handle degradation of equipment
        degradeEquipment(attacker, defender);

        // Display results
        System.out.println(attacker.getName() + " attacks " + defender.getName() +
                " for " + finalDamage + " damage.");
        if (defender.isKO()) {
            System.out.println(defender.getName() + " is KO!");
        }
    }

    // Degrade weapon and armor durability
    private void degradeEquipment(Character attacker, Character defender) {
        // Reduce attacker’s weapon durability
        if (attacker.getWeapon() != null) {
            attacker.getWeapon().reduceDurability();
            if (attacker.getWeapon().isBroken()) {
                attacker.equipWeapon(null); // Remove broken weapon
                System.out.println(attacker.getName() + "'s weapon is broken!");
            }
        }

        // Reduce defender’s armor durability
        if (defender.getArmor() != null) {
            defender.getArmor().reduceDurability();
            if (defender.getArmor().isBroken()) {
                defender.equipArmor(null); // Remove broken armor
                System.out.println(defender.getName() + "'s armor is broken!");
            }
        }
    }
}
