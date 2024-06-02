package DomainLayer.Role;

public class SystemManager {

    private int member_ID;

    SystemManager(int member_ID)
    {
        this.member_ID = member_ID;
    }

    public int getMember_ID() {
        return this.member_ID;
    }

}
