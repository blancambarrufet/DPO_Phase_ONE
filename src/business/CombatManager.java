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

    public void combatSimulation() {
        controller.displayMessage("\nStarting simulation...");

        List<Team> availableTeams = teamManager.loadTeams();
        controller.displayTeamsAvailable(availableTeams);

        if (availableTeams.size() < 2) {
            controller.displayMessage("(ERROR) Not enough teams to start a combat.");
            return;
        }

        int teamIndex1 = controller.requestTeamForCombat(1, availableTeams.size()) - 1;
        int teamIndex2 = controller.requestTeamForCombat(2, availableTeams.size()) - 1;

        Team team1 = availableTeams.get(teamIndex1);
        Team team2 = availableTeams.get(teamIndex2);

        controller.displayMessage("\nInitializing teams...\n");

        List<Member> team1Members = team1.getMembers();
        List<Member> team2Members = team2.getMembers();

        initializeTeams(team1Members, team2Members);

        controller.displayTeamInitialization(team1, 1, team1Members);
        controller.displayTeamInitialization(team2, 2, team2Members);

        controller.displayEndRoundMessage();

        executeCombat(team1,team1Members, team2,team2Members);
    }

    public void initializeTeams (List<Member> team1Members, List<Member> team2Members) {
        Random random = new Random();

        for (Member member : team1Members) {
            equipItems(member);
            member.resetDamage();
        }

        for (Member member : team2Members) {
            equipItems(member);
            member.resetDamage();
        }

    }

    private void equipItems(Member member) {
        try {
            itemManager.equipItemsMember(member);
        } catch (PersistanceException e) {
            controller.displayMessage("Error equipping items: " + e.getMessage());
        }
    }

    //Execute Combat between Two Teams
    public void executeCombat(Team team1, List<Member> team1Members, Team team2, List<Member> team2Members) {
        int round = 1;

        // Perform rounds until one team is defeated
        while (!isTeamDefeated(team1Members) && !isTeamDefeated(team2Members)) {
            controller.displayRoundMessage(round);

            //applying defense from the previous turn
            applyDefendingForMembers(team1Members);
            applyDefendingForMembers(team2Members);

            //display the stats in the start of a new round
            controller.displayTeamStats(team1, 1, team1Members);
            controller.displayTeamStats(team2, 2, team2Members);

            //execute the turns of each team
            executeTurn(team1,team1Members, team2, team2Members);
            executeTurn(team2,team2Members, team1, team1Members);

            applyAccumulatedDamage(team1Members);
            applyAccumulatedDamage(team2Members);

            //durabilityChecking(team1Members, team2Members);

            KOChecking(team1Members, team2Members);

            //reset the defending characters after turn ends
            resetDefenseAfterTurn(team1Members);
            resetDefenseAfterTurn(team2Members);

            round++;
        }

        // Step 1: Check if both teams are KO (Tie Condition)
        boolean team1Defeated = isTeamDefeated(team1Members);
        boolean team2Defeated = isTeamDefeated(team2Members);
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

    private void applyAccumulatedDamage(List<Member> team1Members) {
        for(Member member : team1Members) {
            member.updatePendingDamage();
        }
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

//    public void durabilityChecking(List<Member> team1, List<Member> team2) {
//        for (Member member : team1) {
//            degradeEquipment(member);
//        }
//
//        for (Member member : team2) {
//            degradeEquipment(member);
//        }
//    }

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

    public void KOChecking(List<Member> team1Members, List<Member> team2Members) {
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

    // Check if a Team is Defeated
    public boolean isTeamDefeated(List<Member> members) {

        for (Member member : members) {
            if (!member.isKO()) {
                return false;
            }
        }
        return true;
    }

    private void applyDefendingForMembers(List<Member> teamMembers) {
        for (Member member : teamMembers) {
            member.applyDefending(); //apply defense if it was set in the last turn
        }
    }

    private void resetDefenseAfterTurn(List<Member> teamMembers) {
        for(Member member : teamMembers) {
            member.resetDefending();
        }

    }


}
