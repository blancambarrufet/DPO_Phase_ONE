package layerThree;

public class Armor extends Item {
    private Armor(String name, int id, int power, int durability, String type) {
        super(name, id, power, durability, type);
    }

    private int atackValue;

    public int getAtackValue() {
        return atackValue;
    }

    public void setAtackValue(int atackValue) {
        this.atackValue = atackValue;
    }
}
