package business.entities;

public class Character {
    private long id;
    private String name;
    private int weight;
    private Strategy strategy;

    public Character(long id, String name, int weight, Strategy strategy) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.strategy = strategy;
    }

    public long getId() { return id; }
    public String getName() { return name; }
    public int getWeight() { return weight; }

    public void setStrategy(Strategy strategy) {this.strategy = strategy;}

    public String performAction() {return strategy.decideAction(this);}
}
