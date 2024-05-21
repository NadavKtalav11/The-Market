package DomainLayer.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private List<StoreOwner> storeOwnersList;  //todo: move this to repository
    private List<StoreManager> storeManagerList;

    private RoleFacade()
    {
        storeOwnersList = new ArrayList<StoreOwner>();
    }

    public static RoleFacade getInstance() {
        if (roleFacadeInstance == null) {
            roleFacadeInstance = new RoleFacade();
        }
        return roleFacadeInstance;
    }

   public boolean verifyStoreOwner(int storeID, int memberID){
        return getStoreOwner(storeID, memberID) != null;
   }
  
  public StoreOwner getStoreOwner(int storeID, int memberID)
    {
        for(int i=0 ; i<storeOwnersList.size(); i++){
            if(storeOwnersList.get(i).getStore_ID() == storeID &&
                    storeOwnersList.get(i).getMember_ID() == memberID){
                return storeOwnersList.get(i);
            }
        }
        return null;
    }

    public boolean verifyStoreManager(int storeID, int memberID){
        return getStoreManager(storeID, memberID) != null;
    }

    public StoreManager getStoreManager(int storeID, int memberID)
    {
        for(int i=0 ; i<storeManagerList.size(); i++){
            if(storeManagerList.get(i).getStore_ID() == storeID &&
                    storeManagerList.get(i).getMember_ID() == memberID){
                return storeManagerList.get(i);
            }
        }
        return null;
    }

    public List<Integer> getAllStoreRoles(int storeID)
    {
        List<Integer> rolesList = new ArrayList<>();
        for (StoreOwner storeOwner : storeOwnersList)
        {
            if (storeOwner.getStore_ID() == storeID)
                rolesList.add(storeOwner.getMember_ID());
        }
        for (StoreManager storeManager: storeManagerList)
        {
            if(storeManager.getStore_ID() == storeID)
                rolesList.add(storeManager.getMember_ID());
        }
        return rolesList;
    }

    public boolean verifyStoreOwnerIsFounder(int storeID, int memberID){
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwner(int member_ID, int store_ID, boolean founder)
    {
        StoreOwner newStoreOwner = new StoreOwner(member_ID, store_ID, founder);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void createStoreManager(int member_ID, int store_ID,
                            boolean inventoryPermissions, boolean purchasePermissions)
    {
        StoreManager newStoreManager = new StoreManager(member_ID, store_ID, inventoryPermissions, purchasePermissions);
        addNewStoreManagerToTheMarket(newStoreManager);
    }

    public void updateStoreManagerPermissions(int member_ID, int store_ID,
                                   boolean inventoryPermissions, boolean purchasePermissions)
    {
        StoreManager newStoreManager = new StoreManager(member_ID, store_ID, inventoryPermissions, purchasePermissions);
        addNewStoreManagerToTheMarket(newStoreManager);
        getStoreManager(store_ID, member_ID).setPermissions(inventoryPermissions, purchasePermissions);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        storeOwnersList.add(storeOwner);
    }
    private void addNewStoreManagerToTheMarket(StoreManager storeManager)
    {
        storeManagerList.add(storeManager);
    }

}
