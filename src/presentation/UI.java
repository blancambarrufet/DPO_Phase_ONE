package presentation;

import business.entities.Item;

import java.util.ArrayList;
import java.util.Scanner;

public class UI {

    Scanner scanner = new Scanner(System.in);

    public void displayPersistanceManagement() {
        // TODO implement here

        System.out.println("  ___                      _    ___     ___         _ ");
        System.out.println(" / __|_  _ _ __  ___ _ _  | |  / __|   | _ )_ _ ___| |");
        System.out.println(" \\__ \\ || | '_ \\/ -_) '_| | |__\\__ \\_  | _ \\ '_/ _ \\_|");
        System.out.println(" |___/\\_,_| .__/\\___|_|   |____|___( ) |___/_| \\___(_)");
        System.out.println("          |_|                      |/                 ");
        System.out.println();

        System.out.println("Welcome to Super LS, Bro! Simulator.");
        System.out.println("Verifying local files...");

        //if files exists:
        System.out.println("Files OK.");
        System.out.println("Starting program... ");

        //else if doesnt exists:
        System.out.println("Error: The characters.json file can’t be accessed.");
        System.out.println("Shutting down...");
    }


    public MainMenu printMainMenu() {
        // TODO implement here
        int option = 0 ;
        do {
            System.out.println("\t1) List Characters");
            System.out.println("\t2) Manage Teams");
            System.out.println("\t3) List Items");
            System.out.println("\t4) Simulate Combat");
            System.out.println();
            System.out.println("\t5) Exit");


            String select;
            select = scanner.nextLine();
            option = Integer.parseInt(select);

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

        } while (option != 5);

        return null;
    }

    public TeamManagementMenu printTeamMenu() {
        int option = 0;
        do {
            System.out.println("\t1) Create Team");
            System.out.println("\t2) List Teams");
            System.out.println("\t3) Delete Team");
            System.out.println("\t4) Back");
            System.out.println();

            String select = scanner.nextLine();
            option = Integer.parseInt(select);

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
        } while (option != 4);
        return null;
    }

    public void executeMenuSelection() {
        // TODO implement here

    }


    public String requestTeamInfo() {
        // TODO implement here
        return "";
    }

    public String requestCharacterInfo() {
        // TODO implement here
        return "";
    }


    public String requestItemInfo() {
        // TODO implement here
        return "";
    }

    public void displayItemInfo(ArrayList<Item> items) {
        // TODO implement here
        for (Item item : items) {
            System.out.println("ID:        " + item.getId());
            System.out.println("NAME:      " + item.getName());
            System.out.println("CLASS:     " + item.getType());  // Using 'type' as the class
            System.out.println("POWER:     " + item.getPower());
            System.out.println("DURABILITY:" + item.getDurability());
            System.out.println();  // Adding a blank line between items
        }
    }
    public String requestCombatTeam() {
        // TODO implement here
        return "";
    }

    public void displayCombat() {
        // TODO implement here

    }

    public void printCombatResult() {
        // TODO implement here

    }

    public void displayStats() {
        // TODO implement here

    }

    public boolean validatePersistence() {
        File file = new File("data/characters.json");
        if (file.exists() && file.canRead()) {
            System.out.println("Files OK.");
            System.out.println("Starting program... ");
            return true;
        } else {
            System.out.println("Error: The characters.json file can’t be accessed.");
            System.out.println("Shutting down...");
            return false;
        }
    }


}
