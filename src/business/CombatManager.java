package business;

import business.entities.*;
import business.entities.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatManager {


    public void initializeTeams (List<Character> team1Characters, List<Character> team2Characters, List<Weapon> weapons, List<Armor> armors) {
        Random random = new Random();

        for (Character character : team1Characters) {
            equipItems(character, weapons,armors, random);
            character.resetDamage();
        }

        for (Character character : team2Characters) {
            equipItems(character, weapons,armors, random);
            character.resetDamage();
        }

    }

    private void equipItems(Character character, List<Weapon> weapons, List<Armor> armors, Random random) {

        if (!weapons.isEmpty()) {
            Weapon randomWeapon = weapons.get(random.nextInt(weapons.size()));
            character.equipWeapon(randomWeapon);
        }

        if (!armors.isEmpty()) {
            Armor randomArmor = armors.get(random.nextInt(armors.size()));
            character.equipArmor(randomArmor);
        }
    }


    // Execute Combat between Two Teams
    public void executeCombat(Team teamOne, Team teamTwo) {
        System.out.println("Combat Start: " + teamOne.getName() + " vs " + teamTwo.getName());

        // Perform rounds until one team is defeated
        while (!isTeamDefeated(teamOne) && !isTeamDefeated(teamTwo)) {
            executeTurn(teamOne, teamTwo);
            executeTurn(teamTwo, teamOne); // Both teams attack in turns
        }

        // Display combat results
        displayCombatResult(teamOne, teamTwo);
    }

    // Execute a Turn for a Team
    private void executeTurn(Team attackers, Team defenders) {
        /*
        for (Character attacker : attackers.getMembers()) {
            if (!attacker.isKO()) { // Only active characters can attack
                // Find a defender to attack
                Character defender = selectTarget(defenders.getMembers());
                if (defender != null) {
                    performAttack(attacker, defender); // Perform the attack
                }
            }
        }

         */
    }

    // Select a Target for Attack
    private Character selectTarget(List<Character> defenders) {
        for (Character defender : defenders) {
            if (!defender.isKO()) { // Target first active defender
                return defender;
            }
        }
        return null; // No valid target found
    }

    // Perform an Attack Between Characters
    private void performAttack(Character attacker, Character defender) {
        // Calculate attack and defense values
        double attackDamage = attacker.calculateAttack();
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        // Apply damage to defender
        defender.takeDamage(finalDamage);

        // Display attack results
        System.out.println(attacker.getName() + " attacks " + defender.getName() +
                " for " + finalDamage + " damage!");

        // Check KO Status
        if (defender.isKO()) {
            System.out.println(defender.getName() + " is KO!");
        }

        // Degrade weapon and armor
        degradeEquipment(attacker, defender);
    }

    // Degrade Equipment (Weapon and Armor)
    private void degradeEquipment(Character attacker, Character defender) {
        // Degrade attacker's weapon
        if (attacker.getWeapon() != null) {
            attacker.getWeapon().reduceDurability();
            if (attacker.getWeapon().isBroken()) {
                attacker.equipWeapon(null); // Remove broken weapon
                System.out.println(attacker.getName() + "'s weapon is broken!");
            }
        }

        // Degrade defender's armor
        if (defender.getArmor() != null) {
            defender.getArmor().reduceDurability();
            if (defender.getArmor().isBroken()) {
                defender.equipArmor(null); // Remove broken armor
                System.out.println(defender.getName() + "'s armor is broken!");
            }
        }
    }

    // Check if a Team is Defeated
    public boolean isTeamDefeated(Team team) {
        /*
        return team.getMembers().stream().allMatch(Character::isKO);

         */

        return true;
    }

    // Display Combat Results
    private void displayCombatResult(Team teamOne, Team teamTwo) {
        System.out.println("Combat Result:");
        displayTeamStatus(teamOne);
        displayTeamStatus(teamTwo);

        if (isTeamDefeated(teamOne) && isTeamDefeated(teamTwo)) {
            System.out.println("It's a tie! Both teams are KO.");
        } else if (isTeamDefeated(teamOne)) {
            System.out.println(teamTwo.getName() + " wins!");
        } else {
            System.out.println(teamOne.getName() + " wins!");
        }
    }

    // Display Team Status After Combat
    private void displayTeamStatus(Team team) {
        /*
        System.out.println("Team: " + team.getName());
        team.getMembers().forEach(member -> {
            System.out.print(member.getName() + " - ");
            System.out.println(member.isKO() ? "KO" : "Active");
        });

         */
    }
}
