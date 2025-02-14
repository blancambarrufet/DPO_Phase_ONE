package business;

import business.entities.*;
import persistance.exceptions.PersistanceException;
import presentation.Controller;

import java.util.List;
import java.util.Random;

public class CombatManager {

    private CharacterManager characterManager;
    private ItemManager itemManager;
    private Controller controller;
    private TeamManager teamManager;
    private StatisticsManager statisticsManager;

    public CombatManager(CharacterManager characterManager, ItemManager itemManager,TeamManager teamManager, StatisticsManager statisticsManager) {
        this.characterManager = characterManager;
        this.itemManager = itemManager;
        this.teamManager = teamManager;
        this.statisticsManager = statisticsManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void combatStart(Team team1, Team team2) {
        teamManager.initializeTeam(team1);
        teamManager.initializeTeam(team2);

        controller.displayTeamInitialization(team1, 1);
        controller.displayTeamInitialization(team2, 2);

        controller.displayEndRoundMessage();

        executeCombat(team1, team2);
    }

    //Execute Combat between Two Teams
    private void executeCombat(Team team1, Team team2) {
        int round = 1;

        List<Member> team1Members = team1.getMembers();
        List<Member> team2Members = team2.getMembers();

        // Perform rounds until one team is defeated
        while (!teamManager.isTeamDefeated(team1) && !teamManager.isTeamDefeated(team2)) {
            controller.displayRoundMessage(round);

            //applying defense from the previous turn
            team1.applyDefending();
            team2.applyDefending();

            //display the stats in the start of a new round
            controller.displayTeamStats(team1, 1);
            controller.displayTeamStats(team2, 2);

            //execute the turns of each team
            executeTurn(team1,team1Members, team2, team2Members);
            executeTurn(team2,team2Members, team1, team1Members);

            team1.applyAccumulatedDamage();
            team2.applyAccumulatedDamage();

            KOChecking(team1Members, team2Members);

            //reset the defending characters after turn ends
            team1.resetDefenseAfterTurn();
            team2.resetDefenseAfterTurn();

            round++;
        }

        // Step 1: Check if both teams are KO (Tie Condition)
        boolean team1Defeated = teamManager.isTeamDefeated(team1);
        boolean team2Defeated = teamManager.isTeamDefeated(team2);

        Team winner = null;
        if (team1Defeated && team2Defeated) {
            controller.displayCombatResult(null, team1, team1Members, team2, team2Members); // NULL indicates a tie
        } else {
            winner = team1Defeated ? team2 : team1;
            controller.displayCombatResult(winner, team1, team1Members, team2, team2Members);
        }


        int koTeam1 = numberOfKO(team1Members);
        int koTeam2 = numberOfKO(team2Members);

        String winnerName = " ";
        if (winner!=null) {
             winnerName = winner.getName();
        }

        statisticsManager.recordCombatResult(team1.getName(), team2.getName(), koTeam1, koTeam2, winnerName);
    }

    private int numberOfKO(List<Member> team1Members) {
        int count = 0;
        for (Member member : team1Members) {
            if(member.isKO()) {
                count++;
            }
        }

        return count;
    }

    // Execute a Turn for a Team
    private void executeTurn(Team attackingTeam, List<Member> attackers, Team defendingTeam, List<Member> defenders) {
        for (Member attacker : attackers) {
            if (attacker.isKO()) {
                continue;
            }

            if (attacker.getStrategy().equals("balanced")) {
                if (attacker.getWeapon() == null) {
                    requestWeapon(attacker);
                    controller.displayMessage("\nDEBUG: " + attacker.getName() + " picks " + attacker.getWeapon().getName() + " as a random weapon!!!!.\n");
                }
                else {
                    if (attacker.getArmor() != null) {
                        if (attacker.getDamageTaken() >= 0.5 && attacker.getDamageTaken() <= 1.0) {
                            attacker.defendNextTurn();
                            controller.displayMessage("DEBUG: " + attacker.getName() + " will defend in the next turn.");
                        }
                        else {
                            //Perform the attack
                            Member defender = selectTarget(defendingTeam);
                            if (defender != null) {
                                performAttack(attacker, defender);

                            }
                        }
                    }
                    else {
                        //perform the attack
                        Member defender = selectTarget(defendingTeam);
                        if (defender != null) {
                            performAttack(attacker, defender);
                        }
                    }
                }
            }
        }
    }

    private void requestWeapon(Member member) {
        try {
            itemManager.assignRandomWeapon(member);

        } catch (PersistanceException e) {
            controller.displayMessage("Error equipping weapon: " + e.getMessage());
        }

    }

    // Select a Target for Attack
    private Member selectTarget(Team defendingTeam) {
        try {
            return teamManager.getRandomAvailableDefender(defendingTeam.getName());
        } catch (PersistanceException e) {
            controller.displayMessage("Error selecting target: " + e.getMessage());
            return null;
        }
    }

    // Perform an Attack Between Characters
    private void performAttack(Member attacker, Member defender) {

        // Calculate attack and defense values
        double attackDamage = attacker.calculateAttack();
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        controller.displayExecutionTurn(attacker.getName(), attackDamage, attacker.getWeapon().getName(),finalDamage, defender.getName());

        //Store the damage
        defender.accumulateDamage(finalDamage);

        degradeEquipment(attacker, defender);
    }

    //Degrade Equipment (Weapon and Armor)
    private void degradeEquipment(Member attacker, Member defender) {
        //Reduce attacker's weapon durability
        if (attacker.getWeapon() != null) {

            //reduce the durability by 1 because it has been used
            attacker.getWeapon().reduceDurability();

            if (attacker.getWeapon().isBroken()) {
                controller.displayItemDurabilityBreak(attacker.getName(), attacker.getWeapon().getName());
                attacker.equipWeapon(null); // Remove broken weapon
            }
        }

        //Reduce defender's armor durability
        if (defender.getArmor() != null) {
            //reduce the durability of the armor by 1
            defender.getArmor().reduceDurability();

            if (defender.getArmor().isBroken()) {
                controller.displayItemDurabilityBreak(defender.getName(), defender.getArmor().getName());
                defender.equipArmor(null); // Remove broken armor
            }
        }
    }

    private void KOChecking(List<Member> team1Members, List<Member> team2Members) {
        Random random = new Random();

        for (Member member : team1Members) {
            checkForKO(member, random);
        }

        for(Member member : team2Members) {
            checkForKO(member, random);
        }


    }

    // Check if character is KO
    private void checkForKO(Member member, Random random) {
        if (!member.isKO()) {
            double damageTaken = member.getDamageTaken();

            if (damageTaken > 0) {
                // Random value between 1-200
                double knockOutValue = (random.nextInt(200) + 1) / 100.0;

//                System.out.println("[DEBUG] Checking KO for " + member.getName() +
//                        " | KO Threshold: " + String.format("%.2f", knockOutValue) +
//                        " | Damage Taken: " + String.format("%.2f", damageTaken));

                if (knockOutValue < damageTaken) {
                    member.setKO(true);
                    controller.displayKOMember(member.getName());
                }
            }
        }
    }


}
