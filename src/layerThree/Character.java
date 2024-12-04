package layerThree;

public class Character {
    private int id;
    private String name;
    private int weight;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void newCharacter(int id, String name, int weight){
        this.id = id;
        this.name = name;
        this.weight = weight;
    }



}
