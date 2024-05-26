package DomainLayer.Role;

import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private Map<Integer,List<StoreOwner>> userId_storeOwnersMap;  //todo: move this to repository
    private Map<Integer,List<StoreManager>> userId_storeManagerMap;
    private List<SystemManager> systemManagers ;
    Object storeOwnerLock;
    Object storeManagerLock;
    Object systemManagerLock;


    private RoleFacade()
    {
        userId_storeOwnersMap = new HashMap<>();
        userId_storeManagerMap = new HashMap<>();
        systemManagers = new ArrayList<>();
        storeManagerLock= new Object();
        systemManagerLock= new Object();
        storeOwnerLock = new Object();
    }

    public static synchronized RoleFacade getInstance() {
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

        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = userId_storeOwnersMap.get(memberID);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID() == storeID) {
                    return found;
                }
            }
        }
        return null;
    }

    public boolean verifyStoreManager(int storeID, int memberID){
        return getStoreManager(storeID, memberID) != null;
    }

    public StoreManager getStoreManager(int storeID, int memberID)
    {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = userId_storeManagerMap.get(memberID);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID() == storeID) {
                    return found;
                }
            }
        }

        return null;
    }

    public boolean verifyStoreOwnerIsFounder(int storeID, int memberID){
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwner(int memberId, int store_ID, boolean founder, int nominatorMemberId)
    {
        StoreOwner newStoreOwner = new StoreOwner(memberId, store_ID, founder, nominatorMemberId);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void createStoreManager(int member_ID, int store_ID,
                                   boolean inventoryPermissions, boolean purchasePermissions, int nominatorMemberId)
    {
        StoreManager newStoreManager = new StoreManager(member_ID, store_ID, inventoryPermissions, purchasePermissions, nominatorMemberId);
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
        synchronized (storeManagerLock) {
            int memberId = storeManager.getMember_ID();
            if (userId_storeManagerMap.get(memberId)==null){
                userId_storeManagerMap.put(memberId, new ArrayList<>());
            }
            userId_storeManagerMap.get(memberId).add(storeManager);
        }
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        synchronized (storeOwnerLock) {
            int memberId = storeOwner.getMember_ID();
            if (userId_storeOwnersMap.get(memberId)==null){
                userId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            userId_storeOwnersMap.get(memberId).add(storeOwner);
        }
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
        Map<Integer, List<Integer>> managersAuthorizations= new HashMap<>();
        synchronized (storeManagerLock) {
            for (Integer memberId : userId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:userId_storeManagerMap.get(memberId)) {
                    if (currStoreManager.getStore_ID() == storeID) {
                        managersAuthorizations.put(memberId, currStoreManager.getAuthorizations());
                    }
                }
            }
        }
        return managersAuthorizations;
    }

    public List<Integer> getAllStoreManagers(int storeID)
    {
        List<Integer> storeManagers = new ArrayList<>();
        synchronized (storeManagerLock) {
            for (Integer memberId : userId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:userId_storeManagerMap.get(memberId)) {
                    if (currStoreManager.getStore_ID() == storeID) {
                        storeManagers.add(currStoreManager.getMember_ID());
                    }
                }
            }
        }
        return storeManagers;
    }

    public List<Integer> getAllStoreOwners(int storeID)
    {
        List<Integer> storeOwners = new ArrayList<>();
        synchronized (storeOwnerLock) {
            for (Integer memberId : userId_storeOwnersMap.keySet()) {
                for (StoreOwner currStoreOwner:userId_storeOwnersMap.get(memberId)) {
                    if (currStoreOwner.getStore_ID() == storeID) {
                        storeOwners.add(currStoreOwner.getMember_ID());
                    }
                }
            }
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

    /*public void getStoresByOwners(List<StoreOwner> storeOwnersList) {
        synchronized (storeOwnersList) {
            this.storeOwnersList = storeOwnersList;
        }
    }*/

    public boolean verifyMemberIsSystemManager(int member_ID)
    {
        synchronized (systemManagerLock) {
            for (SystemManager systemManager : systemManagers) {
                if (systemManager.getMember_ID() == member_ID)
                    return true;
            }
        }
        return false;
    }
}
