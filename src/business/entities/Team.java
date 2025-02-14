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

    public void applyDefending() {
        for (Member member : members) {
            member.applyDefending(); //apply defense if it was set in the last turn
        }
    }

    public void applyAccumulatedDamage() {
        for(Member member : members) {
            member.updatePendingDamage();
        }
    }

    public void resetDefenseAfterTurn() {
        for(Member member : members) {
            member.resetDefending();
        }

    }
}
