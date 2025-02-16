package business.entities;

public class Statistics {
    private String name;
    private int games_played;
    private int games_won;
    private int KO_done;
    private int KO_received;

    public Statistics(String name) {
        this.name = name;
        this.games_played = 0;
        this.games_won = 0;
        this.KO_done = 0;
        this.KO_received = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGames_played() {
        return games_played;
    }

    public int getGames_won() {
        return games_won;
    }

    public int getKO_done() {
        return KO_done;
    }

    public int getKO_received() {
        return KO_received;
    }

    public void incrementGamesPlayed() { games_played++; }
    public void incrementGamesWon() { games_won++; }
    public void incrementKOMade(int increment) { KO_done += increment; }
    public void incrementKOReceived(int increment ) { KO_received+=increment; }

}
