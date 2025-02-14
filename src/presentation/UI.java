package presentation;

import business.entities.*;
import business.entities.Character;

import java.util.List;
import java.util.Scanner;

public class UI {

    Scanner scanner = new Scanner(System.in);

    public boolean validatePersistence(boolean charactersOk, boolean itemsOk, boolean teamsOk, boolean statsOk) {
        System.out.println("  ___                      _    ___     ___         _ ");
        System.out.println(" / __|_  _ _ __  ___ _ _  | |  / __|   | _ )_ _ ___| |");
        System.out.println(" \\__ \\ || | '_ \\/ -_) '_| | |__\\__ \\_  | _ \\ '_/ _ \\_|");
        System.out.println(" |___/\\_,_| .__/\\___|_|   |____|___( ) |___/_| \\___(_)");
        System.out.println("          |_|                      |/                 ");
        System.out.println();

        System.out.println("Welcome to Super LS, Bro! Simulator.");
        System.out.println("Verifying local files...");

        // Check errors
        if (!charactersOk && !itemsOk) {
            System.out.println("Error: The characters.json and items.json files can’t be accessed.");
        } else if (!charactersOk) {
            System.out.println("Error: The characters.json file can’t be accessed.");
        } else if (!itemsOk) {
            System.out.println("Error: The items.json file can’t be accessed.");
        } else {
            System.out.println("Files OK.");
        }


        if (!teamsOk) {
            System.out.println("Warning: teams.json could not be loaded. No teams will be available.");
        }

        if (!statsOk) {
            System.out.println("Warning: stats.json could not be loaded. No statistics will be available.");
        }

        if (!charactersOk || !itemsOk) {
            System.out.println("Shutting down...");
            return false; // Stop execution if required files are missing
        }

        System.out.println("Starting program... \n");
        return true; // Continue if files are OK
    }

    public MainMenu printMainMenu() {
        int option = 0 ;
        do {
            System.out.println("\t1) List Characters");
            System.out.println("\t2) Manage Teams");
            System.out.println("\t3) List Items");
            System.out.println("\t4) Simulate Combat");
            System.out.println();
            System.out.println("\t5) Exit");
            System.out.print("\nChoose an option: ");


            String select;
            select = scanner.nextLine();
            option = Integer.parseInt(select);

            try {
                switch (option) {
                    case 1:
                        return MainMenu.LIST_CHARACTERS;
                    case 2:
                        return MainMenu.MANAGE_TEAMS;
                    case 3:
                        return MainMenu.LIST_ITEMS;
                    case 4:
                        return MainMenu.SIMULATE_COMBAT;
                    case 5:
                        return MainMenu.EXIT;
                    default:
                        System.out.println("(ERROR) The option is not a valid.");
                }
            } catch (NumberFormatException e) {
                System.out.println("(ERROR) Invalid input. Please enter a number.");
            }

        } while (option != 5);

        return null;
    }

    public int inputScanner(int min, int max, String parameter) {
        Scanner scanner = new Scanner(System.in);
        int option;
        do {
            while (!scanner.hasNextInt()) {
                System.out.println("\tInvalid format for " + parameter + ".");
                System.out.print("\tPlease enter a valid " + parameter + ": ");
                scanner.next();
            }

            option = scanner.nextInt();
            if (option < min|| option > max) {
                System.out.print("\tInvalid option. Please enter a number between " + min + " and " + max + ":");
            }

        }while (option < min || option > max);
        return option;
    }

    public TeamManagementMenu printTeamMenu() {
        int option = 0;
        do {
            System.out.println("\nTeam management.");
            System.out.println("\t1) Create a Team");
            System.out.println("\t2) List Teams");
            System.out.println("\t3) Delete a Team\n");
            System.out.println("\t4) Back");
            System.out.print("\nChoose an option: ");

            option = inputScanner(1, 4, "Team Management");

            try {
                switch (option) {
                    case 1:
                        return TeamManagementMenu.CREATE_TEAM;
                    case 2:
                        return TeamManagementMenu.LIST_TEAM;
                    case 3:
                        return TeamManagementMenu.DELETE_TEAM;
                    case 4:
                        return TeamManagementMenu.BACK;
                    default:
                        System.out.println("(ERROR) Invalid option.");
                }
            } catch (NumberFormatException e) {
                System.out.println("(ERROR) Invalid input. Please enter a number.");
            }
        } while (option != 4);
        return null;
    }

    //*************************************************
    //********Functions for 2.1) Team Creation ********
    //*************************************************

    public String requestTeamInfo() {
        System.out.print("\nPlease enter the team's name: ");
        return scanner.nextLine().trim();
    }

    public String requestCharacterName(int index) {
        System.out.print("\nPlease enter name or id for character #" + index +" : ");
        return scanner.nextLine().trim();
    }

    public String requestStrategy(int index) {
        System.out.println("Game strategy for character #" + index + "?");
        System.out.println("\t1) Balanced");
        System.out.println("\t2) Offensive");
        System.out.println("\t3) Defensive");
        System.out.println("\t4) Sniper");
        System.out.print("\nChoose an option: ");

        while (true) {
            try {
                int option = inputScanner(1, 4, "Strategy");

                switch (option) {
                    case 1:
                        return "balanced";
                    case 2:
                        return "offensive";
                    case 3:
                        return "defensive";
                    case 4:
                        return "sniper";
                    default:
                        System.out.println("(ERROR) Invalid option. Please choose between 1 and 4.");
                }
            } catch (NumberFormatException e) {
                System.out.println("(ERROR) Invalid input. Please enter a number.");
            }
        }
    }

    public void errorCreateTeam(String name){
        System.out.println("\nWe are sorry '" + name + "' is not in the system.");
    }

    //*************************************************
    //********Functions for 1) List Characters ********
    //*************************************************

    public int requestCharacterOption(int optionThreshold) {
        int option;

        do {
            System.out.print("\nChoose an option: ");
            option = Integer.parseInt(scanner.nextLine());

            if (option >= 0 && option <= optionThreshold) {
                return option;
            }
            else {
                System.out.println("(ERROR) Invalid option. Please select a valid number.");
            }

        } while (option <= optionThreshold && option >= 0);

        return 0;
    }


    public int requestItemOption(int optionThreshold) {
        int option;

        do {
            System.out.print("\nChoose an option: ");
            option = Integer.parseInt(scanner.nextLine());

            if (option >= 0 && option <= optionThreshold) {
                return option;
            }
            else {
                System.out.println("(ERROR) Invalid option. Please select a valid number.");
            }

        } while (option <= optionThreshold && option >= 0);

        return 0;
    }

    public int displayItemsList(List<String> items) {
        if (items.isEmpty()) {
            System.out.println("No items available.");
            return 0;
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i +1) + ") " + items.get(i));
            }
            System.out.println("\n0) Back");

            return requestItemOption(items.size());
        }
    }

    public void displayItemDetails(Item item) {
        System.out.println("\n\tID :" + "\t\t" + item.getId());
        System.out.println("\tNAME:\t\t" + item.getName());
        System.out.println("\tCLASS\t\t" + item.getClass().getSimpleName());
        System.out.println("\tPOWER:\t\t" + item.getPower());
        System.out.println("\tDURABILITY: " + item.getDurability());


        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();
    }

    public int displayCharactersList(List<String> characters) {
        if (characters.isEmpty()) {
            System.out.println("No characters available.");
            return 0; // Return 0 to go back
        }
        else {
            for (int i = 0; i < characters.size(); i++) {
                System.out.println((i + 1) + ") " + characters.get(i));
            }
            System.out.println("\n0) Back");

            // Ask the user to select an option
            return requestCharacterOption(characters.size());
        }
    }

    public void displayCharacterDetails(Character character, List<String> teams) {
        System.out.println("\n\tID: " + "\t " + character.getId());
        System.out.println("\tNAME:    " + character.getName());
        System.out.println("\tWEIGHT:  " + character.getWeight() + " kg");

        // Display teams
        System.out.println("\tTEAMS:");

        int exist=0;
        if (teams.isEmpty()) {
            System.out.println("\t\tNo teams related.");
        } else {
            for (String teamName : teams) {
                System.out.println("\t\t- " + teamName);
            }
        }

        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();
    }

    public int requestTeamForCombat(int teamNumber, int maxTeams) {
        int option = -1;
        boolean valid = false;

        while (!valid) {
            try {
                System.out.print("Choose team #" + teamNumber + ": ");
                option = Integer.parseInt(scanner.nextLine().trim());
                if (option > 0 && option <= maxTeams) {
                    valid = true;
                }
                else {
                    System.out.println("(ERROR) Invalid number. Please choose between 1 and " + maxTeams + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("(ERROR) Invalid option. Please enter a number.");
            }
        }

        return option;
    }

    public void displayTeamList(List<Team> teams) {
        System.out.println("Looking for available teams...\n");
        if (teams.isEmpty()) {
            System.out.println("\nNo teams available.\n");
        }
        else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("\t" + (i + 1) + ") " + teams.get(i).getName());
            }
        }
        System.out.println();
    }


    public void displayTeamInitialization(Team team, int teamNumber, List<Member> members) {
        System.out.println("\tTeam #" + teamNumber + " - " + team.getName());


        for (Member member : members) {
            // Ensure no null pointer exception occurs
            String weaponName = (member.getWeapon() != null) ? member.getWeapon().getName() : "No Weapon";
            String armorName = (member.getArmor() != null) ? member.getArmor().getName() : "No Armor";

            System.out.println("\t- " + member.getName());
            System.out.println("\t\t   Weapon: " + weaponName);
            System.out.println("\t\t   Armor: " + armorName);
        }
        System.out.println();
    }

    public void displayTeamStats(Team team, int teamNumber, List<Member> members) {
        System.out.println("Team #" + teamNumber + " - " + team.getName());

        for (Member member : members) {
            // Check if the weapon is null before calling getName()
            String status = member.isKO() ? "KO" : (int) (member.getDamageTaken() * 100) + " %";

            String weaponName = (member.getWeapon() != null) ? member.getWeapon().getName() : "no Weapon";
            // Check if the armor is null before calling getName()
            String armorName = (member.getArmor() != null) ? member.getArmor().getName() : "no Armor";


            System.out.println("\t- " + member.getName() + " (" + status + ") " + weaponName + " - " + armorName);
        }
        System.out.println();
    }

    public void displayExecutionTurn(String attacker, double damageAttack, String weapon, double damageReceived, String defender) {
        System.out.println(attacker + " ATTACKS " + defender + " WITH " + weapon + " FOR " + String.format("%.1f", damageAttack) + " DAMAGE!");
        System.out.println("\t" + defender + " RECEIVES " + String.format("%.2f", damageReceived) + " DAMAGE.\n");
    }

    public void displayItemDurabilityBreak(String memberName, String itemName) {
        System.out.println("Oh no! " + memberName + "’s " + itemName + " breaks!\n");
    }

    public void displayMessage(String message) {
        System.out.println(message);
    }

    public void displayRoundMessage(int round) {
        System.out.println("--- ROUND " + round + "! ---\n");
    }

    public void displayKOMember(String memberName) {
        System.out.println(memberName + " flies away! It’s a KO!\n");
    }

    public void displayCombatResult(Team teamWinner, Team team1, List<Member> team1Members, Team team2, List<Member> team2Members) {
        System.out.println("\n--- END OF COMBAT ---\n");

        if (teamWinner == null) {
            System.out.println("It's a tie! Both teams have been eliminated.\n");
        } else {
            System.out.println("... and " + teamWinner.getName() + " wins!\n");
        }

        System.out.println("Team #1 – " + team1.getName());

        for (Member member : team1Members) {

            String status = member.isKO() ? "KO" : (int) (member.getDamageTaken() * 100) + " %";

            System.out.println(" - " + member.getName() + " (" + status + ")");
        }

        System.out.println("\nTeam #2 – " + team2.getName());

        for (Member member : team2Members) {

            String status = member.isKO() ? "KO" : (int) (member.getDamageTaken() * 100) + " %";

            System.out.println(" - " + member.getName() + " (" + status + ")");
        }

        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();


    }

    public int displayTeamOptionList(List<Team> teams) {
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
            return 0;
        } else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("\t" + (i + 1) + ") " + teams.get(i).getName());
            }
            System.out.println("\n\t0) Back");

            return requestTeamOption(teams.size());
        }

    }

    private int requestTeamOption(int maxSize) {
        int option;

        do {
            System.out.print("\nChoose an option: ");
            option = Integer.parseInt(scanner.nextLine());

            if (option >= 0 && option <= maxSize) {
                return option;
            }
            else {
                System.out.println("(ERROR) Invalid option. Please select a valid number.");
            }

        } while (option <= maxSize && option >= 0);

        return 0;
    }

    public void displayTeamDetails(Team selectedTeam) {
        System.out.println("\n\tTeam name: " + selectedTeam.getName() + "\n");

        int i = 0;
        int width = 30;

        for (Member member : selectedTeam.getMembers()) {
            String newName = String.format("%-" + width + "s", member.getName());
            System.out.println("\tCharacter #" + (i + 1) + ": " + newName + "(" + member.getStrategy().toUpperCase() + ")");
            i++;
        }
    }


    public void printStatistics(Statistics stats) {
        int won = stats.getGames_won();
        int played = stats.getGames_played();
        float winRate = (float) (100 * won) /played;

        System.out.println("\n\tCombats played: \t" + played);
        System.out.println("\tCombats won: \t\t" + won);
        System.out.println("\tWin rate: \t\t\t" + (int) winRate + "%");
        System.out.println("\tKOs done: \t\t\t" + stats.getKO_done());
        System.out.println("\tKOs received: \t\t" + stats.getKO_received());

        System.out.print("\n\t<Press any key to continue...>");
        scanner.nextLine();
    }

    public boolean sure(String name) {
        System.out.print("\nAre you sure you want to remove \"" + name + "\" ?");
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        return userInput.equalsIgnoreCase("Yes");
    }

    public void confirmationMessage(String name, boolean sure) {
        if (sure) {
            System.out.println("\n\""+name+"\" has been removed from the system.");
        }
        else {
            System.out.println("\n\""+name+"\" will not be removed from the system.");
        }
    }

    public void displayEndRoundMessage() {
        System.out.println("Combat ready!");
        System.out.println("<Press any key to continue...>");
        scanner.nextLine();
    }
}
