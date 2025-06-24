package business.entities;

/**
 * Represents a copy of a member of a team that will be used to save in the Json File only with those attributes.
 */
public class MemberPrint {
    //id of the Member to be printed in the json file
    private final long id;

    //strategy of the member to be printed in json file
    private final String strategy;

    /**
     * Constructs a MemberPrint object with the specified id and strategy.
     *
     * @param id The id of the member
     * @param strategy The strategy of the member
     */
    public MemberPrint(long id, String strategy) {
        this.id = id;
        this.strategy = strategy;
    }

    /**
     * Get the member id
     *
     * @return The ID of the character.
     */
    public long getId() {
        return id;
    }

    /**
     * Get the member combat strategy.
     *
     * @return The strategy name.
     */
    public String getStrategy() {
        return strategy;
    }

}

