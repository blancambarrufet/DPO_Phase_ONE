package business.entities;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;
    private List<Member> members;

    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }
    public List<Member> getMembers() { return members; }

    public void addMember(Member member) {
        if (members.size() < 4) {
            members.add(member);
        } else {

            //this probably add-in UI
            System.out.println("Team is full. Cannot add more members.");
        }
    }

    public boolean isFull() {
        return members.size() == 4;
    }

    public boolean hasMember(int characterId) {
        return members.stream().anyMatch(member -> member.getCharacterId() == characterId);
    }

    public void removeMember(Member member) {
        members.remove(member);
    }

    // Display team information
    public void displayTeamInfo() {
        System.out.println("Team: " + name);
        for (Member member : members) {
            System.out.println("\tMember ID: " + member.getCharacterId() + ", Strategy: " + member.getStrategy());
        }
    }


}
