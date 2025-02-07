package business;

import business.entities.*;
import presentation.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatManager {

    private CharacterManager characterManager;
    private ItemManager itemManager;
    private Controller controller;
    private TeamManager teamManager;

    public CombatManager(CharacterManager characterManager, ItemManager itemManager,TeamManager teamManager) {
        this.characterManager = characterManager;
        this.itemManager = itemManager;
        this.teamManager = teamManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void combatSimulation() {
        controller.displayMessage("Starting simulation...");

        List<Team> availableTeams = teamManager.getTeams();
        controller.displayTeamsAvailable(availableTeams);

        if (availableTeams.size() < 2) {
            controller.displayMessage("(ERROR) Not enough teams to start a combat.");
            return;
        }

        int teamIndex1 = controller.requestTeamForCombat(1, availableTeams.size()) - 1;
        int teamIndex2 = controller.requestTeamForCombat(2, availableTeams.size()) - 1;

        Team team1 = availableTeams.get(teamIndex1);
        Team team2 = availableTeams.get(teamIndex2);

        controller.displayMessage("\nInitializing teams...");

        List<Member> team1Members = team1.getMembers();
        List<Member> team2Members = team2.getMembers();

        List<Weapon> availableWeapons = itemManager.getAllWeapons();
        List<Armor> availableArmor = itemManager.getAllArmor();

        initializeTeams(team1Members, team2Members, availableWeapons, availableArmor);

        controller.displayTeamInitialization(team1, 1, team1Members);
        controller.displayTeamInitialization(team2, 2, team2Members);

        controller.displayMessage("\nCombat ready!");
        controller.displayMessage("<Press any key to continue...>");
        controller.requestInput(); //requesting the user an input

        executeCombat(team1,team1Members, team2,team2Members);
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
    public void executeCombat(Team team1, List<Member> team1Members, Team team2, List<Member> team2Members) {
        int round = 1;

        // Perform rounds until one team is defeated
        while (!isTeamDefeated(team1Members) && !isTeamDefeated(team2Members)) {
            controller.displayRoundMessage(round);

            controller.displayTeamStats(team1, 1, team1Members);
            controller.displayTeamStats(team2, 2, team2Members);

            executeTurn(team1Members, team2Members);
            executeTurn(team2Members, team1Members);

            durabilityChecking(team1Members, team2Members);

            round++;
        }

        // Display combat results
        //displayCombatResult(teamOne, teamTwo);
    }

    // Execute a Turn for a Team
    private void executeTurn(List<Member> attackers, List<Member> defenders) {
        for (Member attacker : attackers) {
            if (attacker.IsKO()) {
                continue;
            }

            if (attacker.getStrategy().equals("balanced")) {
                if (attacker.getWeapon() == null) {
                    requestWeapon(attacker);
                    controller.displayMessage(attacker.getName() + " requested a weapon.");
                }
                else {
                    if (attacker.getArmor() != null) {
                        if (attacker.getDamageTaken() >= 0.5 && attacker.getDamageTaken() <= 1.0) {
                            attacker.defend();
                            controller.displayMessage("debug: " + attacker.getName() + " is defending the next turn.");
                        }
                        else {
                            //Perform the attack
                            Member defender = selectTarget(defenders);
                            if (defender != null) {
                                performAttack(attacker, defender);

                            }
                        }
                    }
                    else {
                        //perform the attack
                        Member defender = selectTarget(defenders);
                        if (defender != null) {
                            performAttack(attacker, defender);
                        }
                    }
                }
            }
        }
    }

    private void requestWeapon(Member member) {
        Random random = new Random();

        List<Weapon> weapons = itemManager.getAllWeapons();

        Weapon randomWeapon = weapons.get(random.nextInt(weapons.size()));
        member.equipWeapon(randomWeapon);

    }

    // Select a Target for Attack
    private Member selectTarget(List<Member> defenders) {

        Random random = new Random();

        List<Member> availableDefenders = new ArrayList<>();

        for (Member defender : defenders) {
            if (!defender.IsKO()) {
                availableDefenders.add(defender);
            }
        }

        //System.out.println(availableDefenders);

        if (availableDefenders.isEmpty()) {
            return null;
        }

        return availableDefenders.get(random.nextInt(availableDefenders.size()));
    }

    // Perform an Attack Between Characters
    private void performAttack(Member attacker, Member defender) {

        // Calculate attack and defense values
        double attackDamage = attacker.calculateAttack();
        double finalDamage = defender.calculateFinalDamage(attackDamage);

        controller.displayExecutionTurn(attacker.getName(), attackDamage,attacker.getWeapon().getName(),finalDamage, defender.getName());

        // Apply damage to defender
        defender.takeDamage(finalDamage);
    }

    public void durabilityChecking(List<Member> team1, List<Member> team2) {
        for (Member member : team1) {
            degradeEquipment(member);
        }

        for (Member member : team2) {
            degradeEquipment(member);
        }
    }

    //Degrade Equipment (Weapon and Armor)
    private void degradeEquipment(Member member) {
        //check weapon durability
        if (member.getWeapon() != null) {
            //reduce the durability by 1 because it has been used
            member.getWeapon().reduceDurability();

            if (member.getWeapon().isBroken()) {
                controller.displayItemDurabilityBreak(member.getName(), member.getWeapon().getName());
                member.equipWeapon(null); // Remove broken weapon
            }
        }

        //check for armor durability
        if (member.getArmor() != null) {
            //reduce the durability of the armor by 1
            member.getArmor().reduceDurability();

            if (member.getArmor().isBroken()) {
                controller.displayItemDurabilityBreak(member.getName(), member.getArmor().getName());
                member.equipArmor(null); // Remove broken armor
            }
        }
    }

    // Check if a Team is Defeated
    public boolean isTeamDefeated(List<Member> members) {

        for (Member member : members) {
            if (!member.IsKO()) {
                return false;
            }
        }
        return true;
    }


}
