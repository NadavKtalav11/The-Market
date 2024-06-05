package DomainLayer.Role;

public class SystemManager {

    private String member_ID;

    SystemManager(String member_ID)
    {
        this.member_ID = member_ID;
    }

    public String getMember_ID() {
        return this.member_ID;
    }

}
