package business;

import business.entities.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CombatManager {

    private CharacterManager characterManager;
    private ItemManager itemManager;

    public CombatManager(CharacterManager characterManager, ItemManager itemManager) {
        this.characterManager = characterManager;
        this.itemManager = itemManager;
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
    public void executeCombat(List<Member> team1Members, List<Member> team2Members) {
        int round = 1;

        // Perform rounds until one team is defeated
        while (!isTeamDefeated(team1Members) && !isTeamDefeated(team2Members)) {
            executeTurn(team1Members,team2Members);
            executeTurn(team2Members, team1Members);
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
                }
                else {
                    if (attacker.getArmor() != null) {
                        if (attacker.getDamageTaken() >= 0.5 && attacker.getDamageTaken() <= 1.0) {
                            attacker.defend();
                            System.out.println("debug: " + attacker.getName() + " is defending this turn.");
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
            if (defender.IsKO()) {
                availableDefenders.add(defender);
            }
        }

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

        // Apply damage to defender
        defender.takeDamage(finalDamage);

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
    public boolean isTeamDefeated(List<Member> members) {

        for (Member member : members) {
            if (member.IsKO()) {
                return false;
            }
        }
        return true;
    }
}
