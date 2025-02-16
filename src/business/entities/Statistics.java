package business.entities;

/**
 * Represents the statistics of a team, this object will track the number of
 * games played, games won, KOs made, and KOs received.
 * It will be used for updating the statistics of a team after the combat or for display the stats of a team.
 */
public class Statistics {
    //name of the team
    private String name;

    //the number of games played of the team
    private int games_played;

    //the number of games won of the team
    private int games_won;

    //the number of KO done of the team
    private int KO_done;

    //the number of KO received of the team
    private int KO_received;

    /**
     * Constructor of Statistics for a specific team name
     *
     * @param name The name of the team.
     */
    public Statistics(String name) {
        this.name = name;
        this.games_played = 0;
        this.games_won = 0;
        this.KO_done = 0;
        this.KO_received = 0;
    }

    /**
     * Get the name of the team
     *
     * @return The name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the team.
     *
     * @param name The new name of the team.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the number of games played.
     *
     * @return The number of games played.
     */
    public int getGames_played() {
        return games_played;
    }

    /**
     * Get the number of games won.
     *
     * @return The number of games won.
     */
    public int getGames_won() {
        return games_won;
    }

    /**
     * Get the number of KO's made.
     *
     * @return The number of KO's made.
     */
    public int getKO_done() {
        return KO_done;
    }

    /**
     * Get the number of KO's received.
     *
     * @return The number of KO's received.
     */
    public int getKO_received() {
        return KO_received;
    }

    /**
     * Increments the number of games played by one after the combat
     */
    public void incrementGamesPlayed() { games_played++; }
    /**
     * Increments the number of games won by one after the combat
     */
    public void incrementGamesWon() { games_won++; }
    /**
     * Increments the number of KO's made.
     *
     * @param increment The number of KOs to increment.
     */
    public void incrementKOMade(int increment) { KO_done += increment; }
    /**
     * Increments the number of KOs received.
     *
     * @param increment The number of KOs to receive.
     */
    public void incrementKOReceived(int increment ) { KO_received+=increment; }

}
