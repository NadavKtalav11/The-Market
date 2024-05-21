package DomainLayer.Role;

import java.util.HashMap;
import java.util.Map;

public class StoreManager implements Role {

    private int member_ID;
    private int store_ID;
    private boolean inventoryPermissions;
    private boolean purchasePermissions;

    StoreManager(int member_ID, int store_ID, boolean inventoryPermissions, boolean purchasePermissions)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
    }

    public void setPermissions(boolean inventoryPermissions, boolean purchasePermissions){
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
    }

    public int getStore_ID()
    {
        return this.store_ID;
    }

    public int getMember_ID()
    {
        return this.member_ID;
    }

    public boolean hasInventoryPermissions(){
        return this.inventoryPermissions;
    }

    public boolean hasPurchasePermissions(){
        return this.purchasePermissions;
    }
}