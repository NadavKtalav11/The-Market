package DomainLayer.Role;

import Util.ExceptionsEnum;
import Util.UserDTO;
import com.fasterxml.jackson.databind.JsonSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;


    private List<SystemManager> systemManagers;
    private final Object systemManagerLock;

    StoreOwnerRepository storeOwnerRepository;
    StoreManagerRepository storeManagerRepository;


    private RoleFacade() {
        systemManagers = new ArrayList<>();

        storeManagerRepository = new MemoryStoreManagerRepository();
        storeOwnerRepository = new MemoryStoreOwnerRepository();
        systemManagerLock = new Object();

    }

    public static synchronized RoleFacade getInstance() {
        if (roleFacadeInstance == null) {
            roleFacadeInstance = new RoleFacade();
        }
        return roleFacadeInstance;
    }

    public RoleFacade newForTest() {
        roleFacadeInstance = new RoleFacade();
        return roleFacadeInstance;
    }


    public boolean verifyStoreOwner(String storeID, String memberID) {
        return getStoreOwner(storeID, memberID) != null;
    }


    public void verifyStoreOwnerError(String storeID, String memberID) throws Exception {
        if (!verifyStoreOwner(storeID, memberID))
            throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
    }

    public StoreOwner getStoreOwner(String storeID, String memberID) {
        return storeOwnerRepository.get(storeID, memberID);
    }

    public boolean verifyStoreManager(String storeID, String memberID) {
        return getStoreManager(storeID, memberID) != null;
    }

    public StoreManager getStoreManager(String storeID, String memberID) {
        return storeManagerRepository.get(storeID, memberID);
    }

    public boolean verifyStoreOwnerIsFounder(String storeID, String memberID) {
        StoreOwner storeOwner = getStoreOwner(storeID, memberID);
        return storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwner(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        if (verifyStoreOwner(storeId, memberId))
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
            throw new Exception(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString());
        }
    }

    public void updateStoreManagerPermissions(String memberId, String storeId,
                                              boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberID) throws Exception {
        if (verifyStoreManager(storeId, memberId)) {
            if (getStoreManager(storeId, memberId).getNominatorMemberId().equals(nominatorMemberID)) {
                getStoreManager(storeId, memberId).setPermissions(inventoryPermissions, purchasePermissions);
            } else {
                throw new Exception(ExceptionsEnum.notNominatorOfThisManager.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.notManager.toString());
        }
    }

    public boolean managerHasInventoryPermissions(String member_ID, String store_ID) {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasInventoryPermissions();
    }

    public boolean managerHasPurchasePermissions(String member_ID, String store_ID) {
        return verifyStoreManager(store_ID, member_ID) && getStoreManager(store_ID, member_ID).hasPurchasePermissions();
    }

    private void addNewStoreManagerToTheMarket(StoreManager storeManager) {
        storeManagerRepository.add(storeManager);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner) {
        storeOwnerRepository.add(storeOwner);
    }

    public void addNewStoreManagerToTheMarketForTests(StoreManager storeManager) {
        if(!verifyStoreManager(storeManager.getStore_ID(), storeManager.getMember_ID()))
            storeManagerRepository.add(storeManager);
    }

    public void addNewStoreOwnerToTheMarketForTests(StoreOwner storeOwner) {
        if(!verifyStoreOwner(storeOwner.getStore_ID(), storeOwner.getMember_ID()))
            storeOwnerRepository.add(storeOwner);
    }


    public Map<String, String> getInformationAboutStoreRoles(String store_ID) {
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

    public Map<String, List<Integer>> getStoreManagersAuthorizations(String storeID) {
        Map<String, List<Integer>> managersAuthorizations = new HashMap<>();
        for (String memberId : storeManagerRepository.getAllMemberId()) {
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdOwners(memberId)) {
                if (currStoreManager.getStore_ID().equals(storeID)) {
                    managersAuthorizations.put(memberId, currStoreManager.getAuthorizations());
                }
            }
        }
        return managersAuthorizations;
    }

    public List<String> getAllStoreManagers(String storeID) {
        List<String> storeManagers = new ArrayList<>();
        for (String memberId : storeManagerRepository.getAllMemberId()) {
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdOwners(memberId)) {
                if (currStoreManager.getStore_ID().equals(storeID)) {
                    storeManagers.add(currStoreManager.getMember_ID());
                }
            }
        }

        return storeManagers;
    }

    public List<String> getAllStoreOwners(String storeID) {
        List<String> storeOwners = new ArrayList<>();

        for (String memberId : storeOwnerRepository.getAllMemberId()) {
            for (StoreOwner currStoreOwner : storeOwnerRepository.getAllMemberIdOwners(memberId)) {
                if (currStoreOwner.getStore_ID().equals(storeID)) {
                    storeOwners.add(currStoreOwner.getMember_ID());
                }
            }
        }
        return storeOwners;
    }


    public List<String> getStoresByOwner(List<String> stores, String member_ID) {
        /*this function gets list of stores id and member id, and return only stores id in which the member is owner*/

        List<String> storesOwned = new ArrayList<>();

        for (String storeID : stores) {
            if (verifyStoreOwner(storeID, member_ID))
                storesOwned.add(storeID);
        }
        return storesOwned;
    }

    /*public void getStoresByOwners(List<StoreOwner> storeOwnersList) {
        synchronized (storeOwnersList) {
            this.storeOwnersList = storeOwnersList;
        }
    }*/

    public boolean verifyMemberIsSystemManager(String member_ID) {
        synchronized (systemManagerLock) {
            for (SystemManager systemManager : systemManagers) {
                if (systemManager.getMember_ID().equals(member_ID))
                    return true;
            }
        }
        return false;
    }

    public void verifyMemberIsSystemManagerError(String member_ID) throws Exception {
        if (!verifyMemberIsSystemManager(member_ID))
            throw new Exception(ExceptionsEnum.notSystemManager.toString());
    }

    public static void resetInstanceForTests() {
        roleFacadeInstance = null;
    }
}

