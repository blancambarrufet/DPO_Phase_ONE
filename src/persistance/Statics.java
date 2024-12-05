package persistance;

public class Statics {
    private String name;
    private int gamesPlayed;
    private int gamesWon;
    private int koMades;
    private int koReceived;

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

    public int getKoMades() {
        return koMades;
    }

    public void setKoMades(int koMades) {
        this.koMades = koMades;
    }

    public int getKoReceived() {
        return koReceived;
    }

    public void setKoReceived(int koReceived) {
        this.koReceived = koReceived;
    }

    public void statics(String name, int gamesPlayed, int gamesWon, int koMades, int koReceived) {
        this.name = name;
        this.gamesPlayed = gamesPlayed;
        this.gamesWon = gamesWon;
        this.koMades = koMades;
        this.koReceived = koReceived;
    }
}
