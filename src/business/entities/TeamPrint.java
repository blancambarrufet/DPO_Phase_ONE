package business.entities;

import java.util.List;

public class TeamPrint {
    private String name;
    private List<MemberPrint> members;

    public TeamPrint(String name, List<MemberPrint> members) {
        this.name = name;
        this.members = members;
    }

    public List<MemberPrint> getMembers() {
        return members;
    }

    public String getName() {
        return name;
    }


}

