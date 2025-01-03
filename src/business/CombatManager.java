package business;

import business.entities.Character;
import business.entities.Team;

public class CombatManager {

    // Execute a combat turn between two teams
    public void executeCombat(Team teamOne, Team teamTwo) {
        System.out.println("Combat Start: " + teamOne.getName() + " vs " + teamTwo.getName());

        // Perform attacks until one team is defeated
        while (!isTeamDefeated(teamOne) && !isTeamDefeated(teamTwo)) {
            executeTurn(teamOne, teamTwo);
            executeTurn(teamTwo, teamOne); // Both teams attack in turns
        }

        // Display results
        displayCombatResult(teamOne, teamTwo);
    }

    // Simulate a single turn
    private void executeTurn(Team attackers, Team defenders) {
        for (Character attacker : attackers.getMembers()) {
            if (!attacker.isKO()) { // Only active characters can attack
                for (Character defender : defenders.getMembers()) {
                    if (!defender.isKO()) {
                        attack(attacker, defender);
                        break; // Attack one defender at a time
                    }
                }
            }
        }
    }

    // Attack logic between two characters
    private void attack(Character attacker, Character defender) {
        double attackDamage = attacker.calculateAttack();
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        defender.takeDamage(finalDamage);

        // Display attack details
        System.out.println(attacker.getName() + " attacks " + defender.getName() +
                " for " + finalDamage + " damage!");

        if (defender.isKO()) {
            System.out.println(defender.getName() + " is KO!");
        }
    }

    // Check if a team is defeated
    public boolean isTeamDefeated(Team team) {
        return team.getMembers().stream().allMatch(Character::isKO);
    }

    // Display combat result
    public void displayCombatResult(Team teamOne, Team teamTwo) {
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

    // Display team status
    private void displayTeamStatus(Team team) {
        System.out.println("Team: " + team.getName());
        team.getMembers().forEach(member -> {
            System.out.print(member.getName() + " - ");
            System.out.println(member.isKO() ? "KO" : "Active");
        });
    }
}
