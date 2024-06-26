package DomainLayer.Role;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreManager implements Role {

    private String member_ID;
    private String store_ID;
    private List<Integer> authorizations;
    private boolean inventoryPermissions;
    private boolean purchasePermissions;
    private String nominatorMemberId;

    StoreManager(String member_ID, String store_ID, boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId)
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

    public String getNominatorMemberId()
    {
        return this.nominatorMemberId;
    }

    public String getStore_ID()
    {
        return this.store_ID;
    }

    public String getMember_ID()
    {
        return this.member_ID;
    }

    public String getNominatorId() {
        return this.nominatorMemberId;
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