package DomainLayer.Role;

import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private Map<Integer,List<StoreOwner>> memberId_storeOwnersMap;  //todo: move this to repository
    private Map<Integer,List<StoreManager>> memberId_storeManagerMap;
    private List<SystemManager> systemManagers ;
    Object storeOwnerLock;
    Object storeManagerLock;
    Object systemManagerLock;


    private RoleFacade()
    {
        memberId_storeOwnersMap = new HashMap<>();
        memberId_storeManagerMap = new HashMap<>();
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
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberID);
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
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberID);
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

    public void createStoreOwner(int memberId, int storeId, boolean founder, int nominatorMemberId) throws Exception {
        if (!verifyStoreOwner(storeId, memberId)) {
            StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId);
            addNewStoreOwnerToTheMarket(newStoreOwner);
        } else {
            throw new Exception("Member is already owner of this store");
        }
    }

    public void createStoreManager(int memberId, int storeId,
                                   boolean inventoryPermissions, boolean purchasePermissions, int nominatorMemberId) throws Exception {
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId);
            addNewStoreManagerToTheMarket(newStoreManager);
        } else {
            throw new Exception("Member already has a role in this store");
        }
    }

    public void updateStoreManagerPermissions(int memberId, int storeId,
                                      boolean inventoryPermissions, boolean purchasePermissions, int nominatorMemberID) throws Exception {
        if (verifyStoreManager(storeId, memberId)) {
            if (getStoreManager(storeId, memberId).getNominatorMemberId() == nominatorMemberID) {
                getStoreManager(storeId, memberId).setPermissions(inventoryPermissions, purchasePermissions);
            } else {
                throw new Exception("Store owner is not the store manager's nominator");
            }
        } else {
            throw new Exception("User is not a manager of this store");
        }
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
            if (memberId_storeManagerMap.get(memberId)==null){
                memberId_storeManagerMap.put(memberId, new ArrayList<>());
            }
            memberId_storeManagerMap.get(memberId).add(storeManager);
        }
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        synchronized (storeOwnerLock) {
            int memberId = storeOwner.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)==null){
                memberId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            memberId_storeOwnersMap.get(memberId).add(storeOwner);
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
            for (Integer memberId : memberId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:memberId_storeManagerMap.get(memberId)) {
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
            for (Integer memberId : memberId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:memberId_storeManagerMap.get(memberId)) {
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
            for (Integer memberId : memberId_storeOwnersMap.keySet()) {
                for (StoreOwner currStoreOwner:memberId_storeOwnersMap.get(memberId)) {
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
