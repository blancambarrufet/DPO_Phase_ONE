package business.entities;

public class Member {
    private long id;
    private String strategy;

    public enum Strategy { //THIS SHOULD BE IN A CLASS ENUM
        BALANCED, AGGRESSIVE, DEFENSIVE
    }

    public Member(long id, String strategy) {
        this.id = id;
        this.strategy = strategy;
    }

    // Getters and Setters
    public long getCharacterId() { return id; }
    public void setCharacterId(long characterId) { this.id = characterId; }

    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }

}
