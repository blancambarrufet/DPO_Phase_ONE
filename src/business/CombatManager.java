package business;

import business.entities.*;
import business.entities.Character;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatManager {

    private CharacterManager characterManager;

    public CombatManager(CharacterManager characterManager) {
        this.characterManager = characterManager;
    }

    public void initializeTeams (List<Member> team1Members, List<Member> team2Members, List<Weapon> weapons, List<Armor> armors) {
        Random random = new Random();

        for (Member member : team1Members) {
            equipItems(member, weapons,armors, random);
            member.resetDamage();
        }

        for (Member member : team2Members) {
            equipItems(member, weapons,armors, random);
            member.resetDamage();
        }

    }

    private void equipItems(Member member, List<Weapon> weapons, List<Armor> armors, Random random) {

        if (!weapons.isEmpty()) {
            Weapon randomWeapon = weapons.get(random.nextInt(weapons.size()));
            member.equipWeapon(randomWeapon);
        }

        if (!armors.isEmpty()) {
            Armor randomArmor = armors.get(random.nextInt(armors.size()));
            member.equipArmor(randomArmor);
        }
    }



    //Execute Combat between Two Teams
    public void executeCombat(Team teamOne, Team teamTwo) {
        int round = 1;

        // Perform rounds until one team is defeated
        while (!isTeamDefeated(teamOne) && !isTeamDefeated(teamTwo)) {
            executeTurn(teamOne, teamTwo);
            executeTurn(teamTwo, teamOne); // Both teams attack in turns
            round++;
        }

        // Display combat results
        displayCombatResult(teamOne, teamTwo);
    }

    // Execute a Turn for a Team
    private void executeTurn(Team attackers, Team defenders) {

        Random random = new Random();

        List<Member> atackMember = attackers.getMembers();
        List<Member> defendMembers = defenders.getMembers();
        for (Member attacker : atackMember) {
            if (!attacker.isKO()) {
                // Find a defender to attack
                Member defender = selectTarget(defendMembers);
                if (defender != null) {
                    performAttack(attacker, defender); //Perform the attack
                }
            }
        }

    }

    // Select a Target for Attack
    private Member selectTarget(List<Member> defenders) {
        for (Member defender : defenders) {
            if (!defender.isKO()) { // Target first active defender
                return defender;
            }
        }
        return null; // No valid target found
    }

    // Perform an Attack Between Characters
    private void performAttack(Member attacker, Member defender) {

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
    private void degradeEquipment(Member attacker, Member defender) {
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
