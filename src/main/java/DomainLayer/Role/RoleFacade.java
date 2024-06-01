package DomainLayer.Role;

import Util.ExceptionsEnum;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private Map<String,List<StoreOwner>> memberId_storeOwnersMap;  //todo: move this to repository
    private Map<String,List<StoreManager>> memberId_storeManagerMap;
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

    public RoleFacade newForTest(){
        roleFacadeInstance= new RoleFacade();
        return roleFacadeInstance;
    }


    public boolean verifyStoreOwner(String storeID, String memberID){
        return getStoreOwner(storeID, memberID) != null;
    }


    

    public void verifyStoreOwnerError(String storeID, String memberID) throws Exception{
        if(!verifyStoreOwner(storeID, memberID))
            throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
    }

    public StoreOwner getStoreOwner(String storeID, String memberID)
    {

        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberID);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID().equals(storeID)) {
                    return found;
                }
            }
        }
        return null;
    }

    public boolean verifyStoreManager(String storeID, String memberID){
        return getStoreManager(storeID, memberID) != null;
    }

    public StoreManager getStoreManager(String storeID, String memberID)
    {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberID);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID().equals(storeID)) {
                    return found;
                }
            }
        }

        return null;
    }

    public boolean verifyStoreOwnerIsFounder(String storeID, String memberID){
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }


   



     public void createStoreOwner(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        if(verifyStoreOwner(storeId, memberId))
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());

        StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void createStoreManager(String memberId, String storeId,
                                   boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId) throws Exception {
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId);
            addNewStoreManagerToTheMarket(newStoreManager);
        } else {
            throw new Exception("Member already has a role in this store");
        }
    }

    public void updateStoreManagerPermissions(String memberId, String storeId,
                                      boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberID) throws Exception {
        if (verifyStoreManager(storeId, memberId)) {
            if (getStoreManager(storeId, memberId).getNominatorMemberId().equals(nominatorMemberID)) {
                getStoreManager(storeId, memberId).setPermissions(inventoryPermissions, purchasePermissions);
            } else {
                throw new Exception("Store owner is not the store manager's nominator");
            }
        } else {
            throw new Exception("User is not a manager of this store");
        }
    }

    public boolean managerHasInventoryPermissions(String member_ID, String store_ID)
    {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasInventoryPermissions();
    }

    public boolean managerHasPurchasePermissions(String member_ID, String store_ID)
    {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasPurchasePermissions();
    }

    private void addNewStoreManagerToTheMarket(StoreManager storeManager)
    {
        String memberId;
        synchronized (storeManagerLock) {
            memberId = storeManager.getMember_ID();
            //if (memberId_storeManagerMap.get(memberId)==null){
            memberId_storeManagerMap.put(memberId, new ArrayList<>());
            memberId_storeManagerMap.get(memberId).add(storeManager);
        }

    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        synchronized (storeOwnerLock) {
            String memberId = storeOwner.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)==null){
                memberId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            memberId_storeOwnersMap.get(memberId).add(storeOwner);
        }
    }

    public Map<String, String> getInformationAboutStoreRoles(String store_ID)
    {
        List<String> storeManagers = getAllStoreManagers(store_ID);
        List<String> storeOwners = getAllStoreOwners(store_ID);

        Map<String, String> storeRoles = new HashMap<>();

        for (String managerId : storeManagers) {
            storeRoles.put(managerId, "manager");
        }

        for (String ownerId : storeOwners) {
            storeRoles.put(ownerId, "owner");
        }

        return storeRoles;
    }

    public Map<String, List<Integer>> getStoreManagersAuthorizations(String storeID)
    {
        Map<String, List<Integer>> managersAuthorizations= new HashMap<>();
        synchronized (storeManagerLock) {
            for (String memberId : memberId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:memberId_storeManagerMap.get(memberId)) {
                    if (currStoreManager.getStore_ID().equals(storeID)){
                        managersAuthorizations.put(memberId, currStoreManager.getAuthorizations());
                    }
                }
            }
        }
        return managersAuthorizations;
    }

    public List<String> getAllStoreManagers(String storeID)
    {
        List<String> storeManagers = new ArrayList<>();
        synchronized (storeManagerLock) {
            for (String memberId : memberId_storeManagerMap.keySet()) {
                for (StoreManager currStoreManager:memberId_storeManagerMap.get(memberId)) {
                    if (currStoreManager.getStore_ID().equals(storeID)) {
                        storeManagers.add(currStoreManager.getMember_ID());
                    }
                }
            }
        }
        return storeManagers;
    }

    public List<String> getAllStoreOwners(String storeID)
    {
        List<String> storeOwners = new ArrayList<>();
        synchronized (storeOwnerLock) {
            for (String memberId : memberId_storeOwnersMap.keySet()) {
                for (StoreOwner currStoreOwner:memberId_storeOwnersMap.get(memberId)) {
                    if (currStoreOwner.getStore_ID().equals(storeID)) {
                        storeOwners.add(currStoreOwner.getMember_ID());
                    }
                }
            }
        }
        return storeOwners;
    }


    public List<String> getStoresByOwner(List<String> stores, String member_ID)
    {
        /*this function gets list of stores id and member id, and return only stores id in which the member is owner*/

        List<String> storesOwned = new ArrayList<>();

        for(String storeID: stores)
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

    public boolean verifyMemberIsSystemManager(String member_ID)
    {
        synchronized (systemManagerLock) {
            for (SystemManager systemManager : systemManagers) {
                if (systemManager.getMember_ID().equals(member_ID))
                    return true;
            }
        }
        return false;
    }

    public static void resetInstanceForTests() {
        roleFacadeInstance = null;
    }

}
