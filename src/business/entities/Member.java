package business.entities;

public class Member {
    private Character caracter;
    private String strategy; //Not sure if this should be a string o...?

    public Character getCaracter() {
        return caracter;
    }

    public void setCaracter(Character caracter) {
        this.caracter = caracter;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public Member(Character caracter, String strategy) {
        this.caracter = caracter;
        this.strategy = strategy;

    }
}
