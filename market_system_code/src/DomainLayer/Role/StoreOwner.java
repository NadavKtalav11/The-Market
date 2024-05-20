package DomainLayer.Role;

public class StoreOwner implements Role{

    private int member_ID;
    private int store_ID;
    public boolean founder;

    StoreOwner(int member_ID, int store_ID, boolean founder)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.founder = founder;
    }

    public int getStore_ID(){
        return this.store_ID;
    }

    public int getMember_ID() {
        return this.member_ID;
    }

}
