package DomainLayer.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private List<StoreOwner> storeOwnersList;  //todo: move this to repository
    private List<StoreManager> storeManagerList;
    private List<SystemManager> systemManagers;

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
        getStoreManager(store_ID, member_ID).setPermissions(inventoryPermissions, purchasePermissions);
    }

    public boolean managerHasInventoryPermissions(int member_ID, int store_ID)
    {
        return getStoreManager(store_ID, member_ID).hasInventoryPermissions();
    }

    public boolean managerHasPurchasePermissions(int member_ID, int store_ID)
    {
        return getStoreManager(store_ID, member_ID).hasPurchasePermissions();
    }

    private void addNewStoreManagerToTheMarket(StoreManager storeManager)
    {
        storeManagerList.add(storeManager);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        storeOwnersList.add(storeOwner);
    }

    public Map<Integer, String> getInformationAboutStoreRoles(int store_ID)
    {
        List<Integer> storeManagers = getAllStoreManagers(store_ID);
        List<Integer> storeOwners = getAllStoreOwners(store_ID);

        Map<Integer, String> storeRoles = new HashMap<>();

        for (Integer managerId : storeManagers) {
            storeRoles.put(managerId, "manager");
        }

        for (Integer ownerId : storeOwners) {
            storeRoles.put(ownerId, "owner");
        }

        return storeRoles;
    }

    public Map<Integer, List<Integer>> getStoreManagersAuthorizations(int storeID)
    {
        Map<Integer, List<Integer>> managersAuthorizations = new HashMap<>();
        for (StoreManager storeManager: storeManagerList)
        {
            if(storeManager.getStore_ID() == storeID)
                managersAuthorizations.put(storeManager.getMember_ID(), storeManager.getAuthorizations());
        }
        return managersAuthorizations;
    }

    public List<Integer> getAllStoreManagers(int storeID)
    {
        List<Integer> storeManagers = new ArrayList<>();
        for (StoreManager storeManager: storeManagerList)
        {
            if(storeManager.getStore_ID() == storeID)
                storeManagers.add(storeManager.getMember_ID());
        }
        return storeManagers;
    }

    public List<Integer> getAllStoreOwners(int storeID)
    {
        List<Integer> storeOwners = new ArrayList<>();
        for (StoreOwner storeOwner : storeOwnersList)
        {
            if (storeOwner.getStore_ID() == storeID)
                storeOwners.add(storeOwner.getMember_ID());
        }
        return storeOwners;
    }


    public List<Integer> getStoresByOwner(List<Integer> stores, int member_ID)
    {
        /*this function gets list of stores id and member id, and return only stores id in which the member is owner*/

        List<Integer> storesOwned = null;

        for(Integer storeID: stores)
        {
            if(verifyStoreOwner(storeID, member_ID))
                storesOwned.add(storeID);
        }
        return storesOwned;
    }

    public void getStoresByOwners(List<StoreOwner> storeOwnersList) {
        this.storeOwnersList = storeOwnersList;
    }

    public boolean verifyMemberIsSystemManager(int member_ID)
    {
        for (SystemManager systemManager : systemManagers)
        {
            if (systemManager.getMember_ID() == member_ID)
                return true;
        }
        return false;
    }
}
