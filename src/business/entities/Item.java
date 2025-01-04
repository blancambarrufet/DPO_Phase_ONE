package business.entities;

public abstract class Item  {
    private String name;
    private long id;
    private int power;
    private int durability;
    private String type;

    public Item(String name, long id, int power, int durability) {
        this.name = name;
        this.id = id;
        this.power = power;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void reduceDurability() {
        if (durability > 0) {
            durability--;
        }
    }

    public boolean isBroken() {
        return durability <= 0;
    }
}
