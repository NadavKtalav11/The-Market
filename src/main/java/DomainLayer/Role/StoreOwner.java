package DomainLayer.Role;

public class StoreOwner implements Role{

    private int member_ID;
    private int store_ID;
    public boolean founder;

    private int nominatorId;

    StoreOwner(int member_ID, int store_ID, boolean founder, int nominatorId)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.founder = founder;
        this.nominatorId = nominatorId;
    }

    public int getStore_ID()
    {
        return this.store_ID;
    }

    public int getNominatorId() {
        return nominatorId;
    }

    @Override
    public int getMember_ID() {
        return this.member_ID;
    }

    public boolean verifyStoreOwnerIsFounder()
    {
        return founder;
    }
}
