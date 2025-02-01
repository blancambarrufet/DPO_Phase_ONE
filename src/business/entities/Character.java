package business.entities;

import java.util.Random;

public class Character {
    private long id;
    private String name;
    private int weight;


    public Character(long id, String name, int weight) {
        this.id = id;
        this.name = name;
        this.weight = weight;
    }

    public long getId() {
        return id;
    }
    public  String getName() {
        return name;
    }
    public int getWeight() {
        return weight;
    }
}
