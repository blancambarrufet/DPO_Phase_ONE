package business.entities;

public class Member {
    private int characterID;
    private String strategy; //Not sure if this should be a string o...?


    public enum Strategy { //THIS SHOULD BE IN A CLASS ENUM
        BALANCED, AGGRESSIVE, DEFENSIVE
    }

    public Member(int characterID, String strategy) {
        this.characterID = characterID;
        this.strategy = strategy;
    }

    // Getters and Setters
    public int getCharacterId() { return characterID; }
    public void setCharacterId(int characterId) { this.characterID = characterId; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

}
