package business.entities;

public class Item  {
    private String name;
    private int id;
    private int power;
    private int durability;
    private String type;

    public Item(String name, int id, int power, int durability, String type) {
        this.name = name;
        this.id = id;
        this.power = power;
        this.durability = durability;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }



}
