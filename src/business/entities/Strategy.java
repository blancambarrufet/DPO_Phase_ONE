package business.entities;

public abstract class Strategy {
    // Common method to be used in all strategies
    public String decideAction(Character character) {
        return chooseAction(character);
    }

    // Abstract method to be implemented by subclasses
    protected abstract String chooseAction(Character character);


}
