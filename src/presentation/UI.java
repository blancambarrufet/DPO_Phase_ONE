package presentation;

import business.entities.*;
import business.entities.Character;

import java.util.List;
import java.util.Scanner;


/**
 * Handles all user interface interactions in the application.
 * This class is responsible for displaying menus, retrieving user input,
 * and showing relevant information such as character details, team management,
 * combat simulation, and game statistics.
 * Key functionalities include:
 * - Displaying and handling the main menu.
 * - Managing teams (creating, listing, and deleting).
 * - Listing characters and displaying their details.
 * - Listing items and showing item details.
 * - Simulating combat between teams.
 * - Displaying team and combat statistics.
 * This class interacts with the user via the console and ensures that
 * valid input is collected before proceeding with game operations.
 */



public class UI {

    private Scanner scanner = new Scanner(System.in);

    //*************************************************
    //************ General functionalities ************
    //*************************************************

    /**
     * Display the validation of the persistence of required JSON files before running the program.
     * The method checks for the existence of characters.json, items.json, teams.json, and stats.json files.
     * If characters.json or items.json is missing, the program shuts down.
     *
     * @param charactersOk Boolean flag indicating if characters.json is accessible.
     * @param itemsOk Boolean flag indicating if items.json is accessible.
     * @param teamsOk Boolean flag indicating if teams.json is accessible.
     * @param statsOk Boolean flag indicating if stats.json is accessible.
     */
    public void displayValidatePersistence(boolean charactersOk, boolean itemsOk, boolean teamsOk, boolean statsOk) {
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
            System.out.println("Error: The characters.json and items.json files can't be accessed.");
        } else if (!charactersOk) {
            System.out.println("Error: The characters.json file can't be accessed.");
        } else if (!itemsOk) {
            System.out.println("Error: The items.json file can't be accessed.");
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
        } else {
            System.out.println("Starting program... \n");
        }
    }

    /**
     * Displays the main menu and retrieves the user's selection.
     * The menu provides options for character listing, team management, item listing, and combat simulation.
     *
     * @return MainMenu The selected option mapped to an enumeration value.
     */
    public MainMenu printMainMenu() {
        int option;
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

        } while (true);

    }

    /**
     * Ensures the user input is within a specified range.
     * This method continuously prompts the user until a valid integer is entered.
     *
     * @param min Minimum valid integer value.
     * @param max Maximum valid integer value.
     * @param parameter Name of the parameter being validated.
     * @return int The validated user input.
     */
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


    //*************************************************
    //************** Functions for Team  **************
    //*************************************************


    /**
     * Displays the team management menu and retrieves the user's selection.
     * Options include creating, listing, and deleting teams, as well as returning to the main menu.
     *
     * @return TeamManagementMenu The selected option mapped to an enumeration value.
     */
    public TeamManagementMenu printTeamMenu() {
        int option;
        do {
            System.out.println("\nTeam management.");
            System.out.println("\t1) Create a Team");
            System.out.println("\t2) List Teams");
            System.out.println("\t3) Delete a Team\n");
            System.out.println("\t4) Back");
            System.out.print("\nChoose an option: ");

            option = inputScanner(1, 4, "Team Management");


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

        } while (true);

    }

    /**
     * Requests the user to enter a team's name.
     *
     * @return String The trimmed team name entered by the user.
     */
    public String requestTeamInfo() {
        System.out.print("\nPlease enter the team's name: ");
        return scanner.nextLine().trim();
    }

    /**
     * Requests the user to enter a character's name or ID.
     *
     * @param index The index of the character (1-based).
     * @return String The trimmed character name or ID entered by the user.
     */
    public String requestCharacterName(int index) {
        System.out.print("\nPlease enter name or id for character #" + index +" : ");
        return scanner.nextLine().trim();
    }

    /**
     * Prompts the user to choose a strategy for a character from the available options.
     *
     * @param index The index of the character (1-based).
     * @return String The chosen strategy as a lowercase string ("balanced", "offensive", "defensive", or "sniper").
     */
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


    /**
     * Displays an error message if a team name is not found in the system.
     *
     * @param name The name of the team that was not found.
     */
    public void errorCreateTeam(String name){
        System.out.println("We are sorry, team" + name + "could not be created.");
    }

    /**
     * Displays the details of a selected team, including its members and their strategies.
     *
     * @param lines The formatted lines with the team name and member details.
     */
    public void displayTeamDetails(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }

    }

    /**
     * Asks the user for confirmation before removing a team.
     *
     * @param name The name of the team to be removed.
     * @return boolean True if the user confirms removal, false otherwise.
     */
    public boolean sure(String name) {
        System.out.print("\nAre you sure you want to remove \"" + name + "\" ?");
        Scanner input = new Scanner(System.in);
        String userInput = input.nextLine();
        return userInput.equalsIgnoreCase("Yes");
    }

    /**
     * Displays a confirmation message based on the user's decision to remove a team.
     *
     * @param name The name of the team.
     * @param sure Boolean indicating whether the team was removed.
     */
    public void confirmationMessage(String name, boolean sure) {
        if (sure) {
            System.out.println("\n\""+name+"\" has been removed from the system.");
        }
        else {
            System.out.println("\n\""+name+"\" will not be removed from the system.");
        }
    }

    /**
     * Displays a list of available teams and allows the user to select one.
     *
     * @param teams A list of available team names.
     * @return int The selected team option (0 if no teams are available or the user chooses to go back).
     */
    public int displayTeamOptionList(List<String> teams) {
        if (teams.isEmpty() ) {
            System.out.println("No teams available.");
            return 0;
        } else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("\t" + (i + 1) + ") " + teams.get(i));
            }
            System.out.println("\n\t0) Back");

            return requestOption(teams.size());
        }

    }

    //*************************************************
    //*********** Functions for Characters ************
    //*************************************************

    /**
     * Requests the user to select an option from the list.
     * Ensures that the selected option is within the valid range.
     *
     * @param optionThreshold The maximum valid option number.
     * @return int The validated user-selected option.
     */
    public int requestOption(int optionThreshold) {
        int option;

        while (true) {
            System.out.print("\nChoose an option: ");
            try {
                option = Integer.parseInt(scanner.nextLine());

                if (option >= 0 && option <= optionThreshold) {
                    return option;
                } else {
                    System.out.println("(ERROR) Invalid option. Please select a number between 0 and " + optionThreshold + ".");
                }
            } catch (NumberFormatException e) {
                System.out.println("(ERROR) Please enter a valid number.");
            }
        }
    }

    /**
     * Displays a list of available characters and prompts the user to select one.
     *
     * @param characters A list of character names.
     * @return int The selected character option (0 if no characters are available or the user chooses to go back).
     */
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
                return requestOption(characters.size());
            }
        }

    /**
     * Displays detailed information about a selected character, including ID, name, weight, and teams.
     *
     * @param character The character object containing character details.
     * @param teams A list of team names associated with the character.
     */
    public void displayCharacterDetails(Character character, List<String> teams) {
            System.out.println("\n\tID: " + "\t " + character.getId());
            System.out.println("\tNAME:    " + character.getName());
            System.out.println("\tWEIGHT:  " + character.getWeight() + " kg");

            // Display teams
            System.out.println("\tTEAMS:");


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


    //*************************************************
    //************* Functions for Items ***************
    //*************************************************

    /**
     * Displays a list of available items and prompts the user to select one.
     *
     * @param items A list of item names.
     * @return int The selected item option (0 if no items are available or the user chooses to go back).
     */
    public int displayItemsList(List<String> items) {
        if (items.isEmpty()) {
            System.out.println("No items available.");
            return 0;
        } else {
            for (int i = 0; i < items.size(); i++) {
                System.out.println((i +1) + ") " + items.get(i));
            }
            System.out.println("\n0) Back");

            return requestOption(items.size());
        }
    }

    /**
     * Displays detailed information about a selected item, including ID, name, class type, power, and durability.
     *
     * @param item The item object containing item details.
     */
    public void displayItemDetails(Item item) {
        System.out.println("\n\tID :" + "\t\t" + item.getId());
        System.out.println("\tNAME:\t\t" + item.getName());
        System.out.println("\tCLASS\t\t" + item.getClass().getSimpleName());
        System.out.println("\tPOWER:\t\t" + item.getPower());
        System.out.println("\tDURABILITY: " + item.getDurability());


        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();
    }



    //*************************************************
    //************* Functions for Combat **************
    //*************************************************


    /**
     * Requests the user to select a team for combat.
     * Ensures the selected team number is within the valid range.
     *
     * @param teamNumber The number assigned to the team in selection order (e.g., Team #1, Team #2).
     * @param maxTeams The maximum number of available teams.
     * @return int The selected team option.
     */
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

    /**
     * Displays a list of available teams.
     *
     * @param teams A list of team names.
     */
    public void displayTeamsAvailable(List<String> teams) {
        System.out.println("Looking for available teams...\n");
        if (teams.isEmpty()) {
            System.out.println("\nNo teams available.\n");
        }
        else {
            for (int i = 0; i < teams.size(); i++) {
                System.out.println("\t" + (i + 1) + ") " + teams.get(i));
            }
        }
        System.out.println();
    }

    /**
     * Displays team initialization details, including assigned weapons and armor.
     *
     * @param team The team being initialized.
     * @param teamNumber The number assigned to the team (e.g., Team #1, Team #2).
     */
    public void displayTeamInitialization(Team team, int teamNumber) {
        System.out.println("\tTeam #" + teamNumber + " - " + team.getName());


        for (Member member : team.getMembers()) {
            // Ensure no null pointer exception occurs
            String weaponName = member.getWeaponName();
            String armorName = member.getArmorName();

            System.out.println("\t- " + member.getName());
            System.out.println("\t\t   Weapon: " + weaponName);
            System.out.println("\t\t   Armor: " + armorName);
        }
        System.out.println();
    }

    /**
     * Displays team stats during combat, including character damage taken and equipment.
     *
     * @param lines The team whose stats are displayed.
     */
    public void displayTeamStats(List<String> lines) {
        for (String line : lines) {
            System.out.println(line);
        }
        System.out.println();
    }

    /**
     * Displays the execution of an attack turn, including damage dealt and received.
     *
     * @param attacker The name of the attacking character.
     * @param damageAttack The amount of damage dealt by the attacker.
     * @param weapon The weapon used in the attack.
     * @param damageReceived The amount of damage received by the defender.
     * @param defender The name of the defending character.
     */
    public void displayExecutionTurn(String attacker, double damageAttack, String weapon, double damageReceived, String defender) {
        System.out.println(attacker + " ATTACKS " + defender + " WITH " + weapon + " FOR " + String.format("%.1f", damageAttack) + " DAMAGE!");
        System.out.println("\t" + defender + " RECEIVES " + String.format("%.2f", damageReceived) + " DAMAGE.\n");
    }

    /**
     * Displays a message when an item breaks due to durability depletion.
     *
     * @param memberName The name of the character whose item broke.
     * @param itemName The name of the broken item.
     */
    public void displayItemDurabilityBreak(String memberName, String itemName) {
        System.out.println("Oh no! " + memberName + "'s " + itemName + " breaks!\n");
    }

    /**
     * Displays a generic message.
     *
     * @param message The message to be displayed.
     */
    public void displayMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays the message for the start of a new combat round.
     *
     * @param round The current round number.
     */
    public void displayRoundMessage(int round) {
        System.out.println("--- ROUND " + round + "! ---\n");
    }

    /**
     * Displays a message when a character is knocked out.
     *
     * @param memberName The name of the character who was knocked out.
     */
    public void displayKOMember(String memberName) {
        System.out.println(memberName + " flies away! It's a KO!\n");
    }

    /**
     * Displays the combat result, showing the winning team and remaining character stats.
     *
     * @param teamWinner The winning team (null if it's a tie).
     * @param team1 The first team that participated in the combat.
     * @param team2 The second team that participated in the combat.
     */
    public void displayCombatResult(Team teamWinner, Team team1,Team team2) {
        System.out.println("\n--- END OF COMBAT ---\n");

        if (teamWinner == null) {
            System.out.println("It's a tie! Both teams have been eliminated.\n");
        } else {
            System.out.println("... and " + teamWinner.getName() + " wins!\n");
        }

        System.out.println("Team #1 – " + team1.getName());

        for (Member member : team1.getMembers()) {

            String status = member.isKO() ? "KO" : Math.round(member.getDamageTaken() * 100) + " %";

            System.out.println(" - " + member.getName() + " (" + status + ")");
        }

        System.out.println("\nTeam #2 – " + team2.getName());

        for (Member member : team2.getMembers()) {

            String status = member.isKO() ? "KO" : Math.round(member.getDamageTaken() * 100) + " %";

            System.out.println(" - " + member.getName() + " (" + status + ")");
        }

        System.out.print("\n<Press any key to continue...>");
        scanner.nextLine();


    }

    /**
     * Displays the overall statistics of a team, including matches played, won, and KOs.
     *
     * @param stats The statistics object containing team performance data.
     */
    public void printStatistics(Statistics stats) {
        if (stats == null) {
            System.out.println("\n\tNo statistics available for this team.");
            System.out.print("\n\t<Press any key to continue...>");
            scanner.nextLine();
            return;
        }
        
        int won = stats.getGames_won();
        int played = stats.getGames_played();
        float winRate = played > 0 ? (float) (100 * won) / played : 0;

        System.out.println("\n\tCombats played: \t" + played);
        System.out.println("\tCombats won: \t\t" + won);
        System.out.println("\tWin rate: \t\t\t" + (int) winRate + "%");
        System.out.println("\tKOs done: \t\t\t" + stats.getKO_done());
        System.out.println("\tKOs received: \t\t" + stats.getKO_received());

        System.out.print("\n\t<Press any key to continue...>");
        scanner.nextLine();
    }

    /**
     * Displays a message indicating the end of a combat round.
     */
    public void displayEndRoundMessage() {
        System.out.println("Combat ready!");
        System.out.println("<Press any key to continue...>");
        scanner.nextLine();
    }
}
