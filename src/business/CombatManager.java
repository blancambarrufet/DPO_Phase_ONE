package business;

import business.entities.*;
import persistance.exceptions.PersistanceException;
import presentation.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Manages combat mechanics between two teams.
 * This class is responsible for handling combat initialization, execution of turns,
 * attack selection, damage calculation, and tracking KO (knockout) status.
 */
public class CombatManager {

    private ItemManager itemManager;
    private Controller controller;
    private TeamManager teamManager;
    private StatisticsManager statisticsManager;

    /**
     * Constructs a CombatManager instance with required dependencies.
     *
     * @param itemManager       Manages items such as weapons and armor.
     * @param teamManager       Manages teams and character assignments.
     * @param statisticsManager Manages combat statistics.
     */
    public CombatManager( ItemManager itemManager,TeamManager teamManager, StatisticsManager statisticsManager) {
        this.itemManager = itemManager;
        this.teamManager = teamManager;
        this.statisticsManager = statisticsManager;
    }

    /**
     * Sets the controller for managing UI interactions.
     *
     * @param controller The UI controller.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Starts a combat between two teams.
     * Initializes teams, displays initial information, and executes combat.
     *
     * @param team1 The first team.
     * @param team2 The second team.
     */
    public void combatStart(Team team1, Team team2) {
        teamManager.initializeTeam(team1);
        teamManager.initializeTeam(team2);

        controller.displayTeamInitialization(team1, 1);
        controller.displayTeamInitialization(team2, 2);

        controller.displayEndRoundMessage();

        executeCombat(team1, team2);
    }

    /**
     * Executes combat rounds between two teams until one is defeated.
     *
     * @param team1 The first team.
     * @param team2 The second team.
     */
    private void executeCombat(Team team1, Team team2) {
        int round = 1;

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
            executeTurn(team1, team2);
            executeTurn(team2, team1);

            team1.applyAccumulatedDamage();
            team2.applyAccumulatedDamage();

            KOChecking(team1, team2);

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
            controller.displayCombatResult(null, team1, team2); // NULL indicates a tie
        } else {
            winner = team1Defeated ? team2 : team1;
            controller.displayCombatResult(winner, team1, team2);
        }


        int koTeam1 = numberOfKO(team1.getMembers());
        int koTeam2 = numberOfKO(team2.getMembers());

        String winnerName = " ";
        if (winner!=null) {
             winnerName = winner.getName();
        }

        statisticsManager.recordCombatResult(team1.getName(), team2.getName(), koTeam1, koTeam2, winnerName);
    }

    /**
     * Counts the number of KO (knocked-out) members in a team.
     *
     * @param teamMembers The list of team members.
     * @return int The number of KO members.
     */
    private int numberOfKO(List<Member> team1Members) {
        int count = 0;
        for (Member member : team1Members) {
            if(member.isKO()) {
                count++;
            }
        }

        return count;
    }

    /**
     * Executes a turn for a team, where each member either attacks or defends.
     *
     * @param attackingTeam The team taking the turn.
     * @param defendingTeam The opposing team.
     */
    private void executeTurn(Team attackingTeam, Team defendingTeam) {

        for (Member attacker : attackingTeam.getMembers()) {
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

    /**
     * Assigns a random weapon to a member.
     *
     * @param member The member receiving the weapon.
     */
    private void requestWeapon(Member member) {
        try {
            itemManager.assignRandomWeapon(member);

        } catch (PersistanceException e) {
            controller.displayMessage("Error equipping weapon: " + e.getMessage());
        }

    }

    /**
     * Selects a random target from the defending team.
     *
     * @param defendingTeam The team being attacked.
     * @return Member The selected target, or null if no valid targets exist.
     */
    private Member selectTarget(Team defendingTeam) {
        List<Member> availableDefenders = new ArrayList<>();

        for (Member member : defendingTeam.getMembers()) {
            if (!member.isKO()) {
                availableDefenders.add(member);
            }
        }

        if (availableDefenders.isEmpty()) return null;

        Random random = new Random();
        int index = random.nextInt(availableDefenders.size());
        return availableDefenders.get(index);
    }

    /**
     * Performs an attack between two members.
     *
     * @param attacker The attacking member.
     * @param defender The defending member.
     */
    private void performAttack(Member attacker, Member defender) {

        // Calculate attack and defense values
        double attackDamage = attacker.calculateAttack();
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        controller.displayExecutionTurn(attacker.getName(), attackDamage, attacker.getWeapon().getName(),finalDamage, defender.getName());

        //Store the damage
        defender.accumulateDamage(finalDamage);

        degradeEquipment(attacker, defender);
    }

    /**
     * Degrades the durability of both the attacker's weapon and the defender's armor.
     *
     * @param attacker The attacking member.
     * @param defender The defending member.
     */
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

    /**
     * Checks if any members have received enough damage to be knocked out.
     *
     * @param team1 The first team.
     * @param team2 The second team.
     */
    private void KOChecking(Team team1, Team team2) {
        Random random = new Random();

        for (Member member : team1.getMembers()) {
            checkForKO(member, random);
        }

        for(Member member : team2.getMembers()) {
            checkForKO(member, random);
        }
    }

    /**
     * Determines if a character is KO based on accumulated damage.
     *
     * @param member The member to check.
     * @param random The random number generator for KO probability.
     */
    private void checkForKO(Member member, Random random) {
        if (!member.isKO()) {
            double damageTaken = member.getDamageTaken();

            if (damageTaken > 0) {
                // Random value between 1-200
                double knockOutValue = (random.nextInt(200) + 1) / 100.0;

                if (knockOutValue < damageTaken) {
                    member.setKO(true);
                    controller.displayKOMember(member.getName());
                }
            }
        }
    }

}
