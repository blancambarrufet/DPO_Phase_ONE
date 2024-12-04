package layerThree;

public class Team {
    private String name;
    private Member[] member;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Member[] getMember() {
        return member;
    }

    public void setMember(Member[] member) {
        this.member = member;
    }

    public Team(String name, Member member) {
        this.name = name;
        this.member = new Member[]{member};
    }

}
