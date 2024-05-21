package DomainLayer.Role;

import java.util.HashMap;
import java.util.Map;

public class StoreManager implements Role {

    private int member_ID;
    private int store_ID;
    private Map<String, Boolean> permissions = new HashMap<String, Boolean>();

    StoreManager(int member_ID, int store_ID, boolean inventoryPermissions, boolean purchasePermissions)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.permissions.put("inventory", inventoryPermissions);
        this.permissions.put("purchase", purchasePermissions);
    }

    public int getStore_ID()
    {
        return this.store_ID;
    }

    public int getMember_ID()
    {
        return this.member_ID;
    }

}