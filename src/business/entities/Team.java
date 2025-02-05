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

    public void setMembers(List<Member> members) { this.members = members; }

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

    public void removeMember(Member member) {
        members.remove(member);
    }




}
