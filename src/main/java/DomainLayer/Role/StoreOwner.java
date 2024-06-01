package DomainLayer.Role;

public class StoreOwner implements Role{

    private String member_ID;
    private String store_ID;
    public boolean founder;

    private String nominatorId;

    StoreOwner(String member_ID, String store_ID, boolean founder, String nominatorId)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.founder = founder;
        this.nominatorId = nominatorId;
    }

    public String getStore_ID()
    {
        return this.store_ID;
    }

    public String getNominatorId() {
        return nominatorId;
    }

    @Override
    public String getMember_ID() {
        return this.member_ID;
    }

    public boolean verifyStoreOwnerIsFounder()
    {
        return founder;
    }
}
