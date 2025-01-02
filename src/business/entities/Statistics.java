package business.entities;

public class Statistics {
    private String name;
    private int gamesPlayed;
    private int gamesWon;
    private int koMade;
    private int koReceived;

    public Statistics(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.koMade = 0;
        this.koReceived = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getGamesWon() {
        return gamesWon;
    }

    public void setGamesWon(int gamesWon) {
        this.gamesWon = gamesWon;
    }

    public int getKoMade() {
        return koMade;
    }

    public void setKoMade(int koMade) {
        this.koMade = koMade;
    }

    public int getKoReceived() {
        return koReceived;
    }

    public void setKoReceived(int koReceived) {
        this.koReceived = koReceived;
    }

    public void incrementGamesPlayed() { gamesPlayed++; }
    public void incrementGamesWon() { gamesWon++; }
    public void incrementKOMade() { koMade++; }
    public void incrementKOReceived() { koReceived++; }

    public String getSummary() {
        return name + " - Played: " + gamesPlayed + ", Won: " + gamesWon +
                ", KO Made: " + koMade + ", KO Received: " + koReceived;
    }

}
