package DomainLayer.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManager implements Role {

    private int member_ID;
    private int store_ID;
    private List<Integer> authorizations;
    private boolean inventoryPermissions;
    private boolean purchasePermissions;
    private int nominatorMemberId;

    StoreManager(int member_ID, int store_ID, boolean inventoryPermissions, boolean purchasePermissions, int nominatorMemberId)
    {
        this.member_ID = member_ID;
        this.store_ID = store_ID;
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
        this.nominatorMemberId = nominatorMemberId;
    }

    public void setPermissions(boolean inventoryPermissions, boolean purchasePermissions){
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
    }

    public int getNominatorMemberId()
    {
        return this.nominatorMemberId;
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

    public boolean hasInventoryPermissions(){
        return this.inventoryPermissions;
    }

    public boolean hasPurchasePermissions(){
        return this.purchasePermissions;
    }
}