package Util;



import java.util.List;

public class StoreManagerDTO {

    private String storeId;
    private String memberId;


    private boolean inventoryPermissions;

    private boolean purchasePermissions;

    private String nominatorMemberId;


    public StoreManagerDTO(String member_ID, String store_ID, boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId)
    {
        this.storeId = store_ID;
        this.memberId = member_ID;
        this.inventoryPermissions = inventoryPermissions;
        this.purchasePermissions = purchasePermissions;
        this.nominatorMemberId = nominatorMemberId;
    }

    public StoreManagerDTO() {

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
        return this.storeId;
    }

    public String getMember_ID()
    {
        return this.memberId;
    }

    public String getNominatorId() {
        return this.nominatorMemberId;
    }


    public boolean hasInventoryPermissions(){
        return this.inventoryPermissions;
    }

    public boolean hasPurchasePermissions(){
        return this.purchasePermissions;
    }
}

