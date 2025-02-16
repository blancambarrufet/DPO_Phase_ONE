package business.entities;

import java.util.List;

/**
 * Represents a copy of a Team to print in the json file
 * Contains the team's name and a list of members.
 */
public class TeamPrint {
    //Name of the team to be printed in the json file
    private final String name;

    //list of members tobe printed in the json file
    private final List<MemberPrint> members;

    /**
     * Constructs a new TeamPrint instance.
     *
     * @param name    The name of the team.
     * @param members A list of team members in a simplified format.
     */
    public TeamPrint(String name, List<MemberPrint> members) {
        this.name = name;
        this.members = members;
    }

    /**
     * Retrieves the name of the team.
     *
     * @return String The name of the team.
     */
    public String getName() {
        return name;
    }


}

