package DomainLayer.Role;

import java.util.List;

public class StoreManager implements Role {

    private int member_ID;
    private int store_ID;
    private List<Integer> authorizations;


    StoreManager(int member_ID, int store_ID)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
    }


    public int getStore_ID()
    {
        return this.store_ID;
    }

    public int getMember_ID()
    {
        return this.member_ID;
    }

    public List<Integer> getAuthorizations(){
        return this.authorizations;
    }
}
