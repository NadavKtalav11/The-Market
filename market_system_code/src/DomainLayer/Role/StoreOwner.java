package DomainLayer.Role;

public class StoreOwner implements Role{

    private String username;
    private int store_ID;
    public boolean founder;

    StoreOwner(String username, int store_ID, boolean founder)
    {
        this.username = username;
        this.store_ID = store_ID;
        this.founder = founder;
    }

    public int getStore_ID(){
        return this.store_ID;
    }

    public String getUsername() {
        return this.username;
    }

}
