package DomainLayer.Role;

import java.util.ArrayList;
import java.util.HashMap;
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
        for(int i=0 ; i<storeOwnersList.size(); i++){
            if(storeOwnersList.get(i).getStore_ID() == storeID &&
                        storeOwnersList.get(i).getMember_ID() == memberID){
                return true;
            }
        }
        return false;
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

    public boolean verifyStoreOwnerIsFounder(int storeID, int memberID){
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwner(int member_ID, int store_ID, boolean founder)
    {
        StoreOwner newStoreOwner = new StoreOwner(member_ID, store_ID, founder);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        storeOwnersList.add(storeOwner);
    }

}
