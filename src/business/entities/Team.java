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



    public void addMember(String Id) {
        //search in the DB for ID
        //if ID not found look for name
        //if name not found return error

    }


    public boolean isFull() {
        return members.size() == 4;
    }

    public boolean hasMember(long characterId) {
        System.out.println("DEBUG: Checking for character ID -> " + characterId);
        for (Member member : members) {
            System.out.println("DEBUG: Member ID -> " + member.getCharacterId());
            if (member.getCharacterId() == characterId) { // Explicit comparison
                System.out.println("DEBUG: Match found for ID " + characterId);
                return true;
            }
        }
        System.out.println("DEBUG: No match found for ID " + characterId);
        return false;
    }


    public void removeMember(Member member) {
        members.remove(member);
    }


}
