package presentation;

import business.entities.Item;
import business.entities.Character;
import business.entities.Member;
import business.entities.Team;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UI {

    Scanner scanner = new Scanner(System.in);

    public boolean validatePersistence(boolean charactersOk, boolean itemsOk) {
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
            System.out.println("Starting program... \n");
            return true; // Continue if files are OK
        }

        System.out.println("Shutting down...");
        return false; // Stop if errors exist
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

    public TeamManagementMenu printTeamMenu() {
        int option = 0;
        do {
            System.out.println("\nTeam management.");
            System.out.println("\t1) Create a Team");
            System.out.println("\t2) List Teams");
            System.out.println("\t3) Delete a Team\n");
            System.out.println("\t4) Back");
            System.out.print("\nChoose an option: ");

            String select = scanner.nextLine();
            option = Integer.parseInt(select);

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
        System.out.print("\t1) Balanced");
        System.out.println("\nChoose an option: ");
        return scanner.nextLine();
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

    public int displayItemsList(ArrayList<Item> items) {
        if (items.isEmpty()) {
            System.out.println("No items available.");
            return 0;
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i +1) + ") " + items.get(i).getName());
            }
            System.out.println("\n0) Back");

            return requestItemOption(items.size());
        }
    }

    public void displayItemDetails(Item item) {
        System.out.println("\n\tID: " + "\t " + item.getId());
        System.out.println("\tNAME:    " + item.getName());
        System.out.println("\tCLASS:  " + item.getClass().getName());
        System.out.println("\tPOWER:  " + item.getPower());
        System.out.println("\tDURABILITY:  " + item.getDurability());


        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();
    }

    // Display Teams Information
    public void displayTeams(ArrayList<Team> teams) {
        if (teams.isEmpty()) {
            System.out.println("No teams available.");
        } else {
            System.out.println("\nTeams:");
            for (Team team : teams) {
                System.out.println("Team: " + team.getName());
                System.out.println("Members:");
                team.getMembers().forEach(member ->
                        System.out.println("\tCharacter ID: " + member.getCharacterId() +
                                ", Strategy: " + member.getStrategy()));
            }
        }
    }

    public int displayCharactersList(ArrayList<Character> characters) {
        if (characters.isEmpty()) {
            System.out.println("No characters available.");
            return 0; // Return 0 to go back
        } else {
            for (int i = 0; i < characters.size(); i++) {
                System.out.println((i + 1) + ") " + characters.get(i).getName());
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
        if (teams.isEmpty()) {
            System.out.println("\t\t\tNo teams related.");
        }
        else {
            for (String team : teams) {
                System.out.println("\t\t\t- " + team);
            }
        }

        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();
    }



    public int requestCombatTeam(int teamNumber, int maxTeams) {
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

    public void displayTeamList(ArrayList<Team> teams) {
        System.out.println("Looking for available teams...\n");
        if (teams.isEmpty()) {
            System.out.println("\nNo teams available.\n");
        }
        else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("\t" + (i + 1) + ") " + teams.get(i).getName());
            }
            System.out.println();
        }
    }


    public void displayTeamDetails(Team team, int teamNumber, List<Character> characters) {
        System.out.println("\n\tTeam #" + teamNumber + " - " + team.getName());

        for (Character character : characters) {
            System.out.println("\t- " + character.getName());
            System.out.println("\t\t   Weapon: " + (character.getWeapon() != null ? character.getWeapon().getName() : "None"));
            System.out.println("\t\t   Armor: " + (character.getArmor() != null ? character.getArmor().getName() : "None"));
        }
    }

    public void displayStatistics(String statistics) {
        System.out.println("\nStatistics: ");
        System.out.println(statistics);
    }



}
