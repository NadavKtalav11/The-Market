package Util;



public class StoreOwnerDTO {

    private String storeId;
    private String memberId;
    private boolean founder;
    private String nominatorId;

    public StoreOwnerDTO(String member_ID, String store_ID, boolean founder, String nominatorId) {
        this.storeId =  store_ID;
        this.memberId = member_ID;
        this.founder = founder;
        this.nominatorId = nominatorId;
    }

    public StoreOwnerDTO() {

    }

    public String getNominatorId() {
        return nominatorId;
    }

    public boolean getFounder() {
        return founder;
    }


    public boolean verifyStoreOwnerIsFounder()
    {
        return founder;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getMemberId() {
        return memberId;
    }
}
