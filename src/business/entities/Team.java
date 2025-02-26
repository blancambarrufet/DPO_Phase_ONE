package business.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a team in the game where it has a name, also consists of a list of members. This is an important
 * class to the game because it can't be a game without the teams.
 */
public class Team {
    //the name of the Team
    private String name;

    //a list of members that composes a team
    private final List<Member> members;

    /**
     * Constructs a Team with the specified name with an empty list of members.
     *
     * @param name The name of the team.
     */
    public Team(String name) {
        this.name = name;
        this.members = new ArrayList<>();
    }

    /**
     * Get the name of the team
     *
     * @return The name of the team
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the team.
     *
     * @param name The new name of the team.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get a list of team members.
     *
     * @return A list of team members.
     */
    public List<Member> getMembers() {
        return members;
    }

    /**
     * Adds a member to the team only if the team has fewer than four members.
     *
     * @param member The member to add.
     */
    public void addMember(Member member) {
        if (members.size() < 4) {
            members.add(member);
        }
    }

    /**
     * Apply the defending flag to members if it was set in the previous turn.
     */
    public void applyDefending() {
        for (Member member : members) {
            member.applyDefending(); //apply defense if it was set in the last turn
        }
    }

    /**
     * Apply accumulated damage to all team members that have pendingDamage at the end of a round
     */
    public void applyAccumulatedDamage() {
        for(Member member : members) {
            member.updatePendingDamage();
        }
    }

    /**
     * Resets the defense flag of all members after a turn.
     */
    public void resetDefenseAfterTurn() {
        for(Member member : members) {
            member.resetDefending();
        }
    }
}
