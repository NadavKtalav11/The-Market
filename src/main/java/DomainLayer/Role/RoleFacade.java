package DomainLayer.Role;

import DomainLayer.Repositories.MemoryStoreManagerRepository;
import DomainLayer.Repositories.MemoryStoreOwnerRepository;
import DomainLayer.Repositories.StoreManagerRepository;
import DomainLayer.Repositories.StoreOwnerRepository;
import Util.ExceptionsEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleFacade {

    private static RoleFacade roleFacadeInstance;


    private List<SystemManager> systemManagers;
    private final Object systemManagerLock;

    StoreOwnerRepository storeOwnerRepository;
    StoreManagerRepository storeManagerRepository;

    StoreManagerRepository managerNominators;
    StoreOwnerRepository ownersNominators;



    public RoleFacade() {
        systemManagers = new ArrayList<>();
        managerNominators = new MemoryStoreManagerRepository() ;
        ownersNominators = new MemoryStoreOwnerRepository() ;
        storeManagerRepository = new MemoryStoreManagerRepository();
        storeOwnerRepository = new MemoryStoreOwnerRepository();
        systemManagerLock = new Object();


    }

    @Autowired
    public RoleFacade(StoreManagerRepository storeManagerRepository, StoreOwnerRepository storeOwnerRepository, StoreManagerRepository managerNominators, StoreOwnerRepository ownersNominators) {
        systemManagers = new ArrayList<>();
        this.managerNominators = managerNominators;
        this.ownersNominators = ownersNominators;
        this.storeManagerRepository = storeManagerRepository;
        this.storeOwnerRepository = storeOwnerRepository;
        systemManagerLock = new Object();
    }

    public static synchronized RoleFacade getInstance() {
        if (roleFacadeInstance == null) {
            roleFacadeInstance = new RoleFacade();
        }
        return roleFacadeInstance;
    }

    public RoleFacade newForTest(){
        roleFacadeInstance = new RoleFacade();
        return roleFacadeInstance;
    }




    public boolean verifyStoreOwner(String storeID, String memberID) {
        return getStoreOwner(storeID, memberID) != null;
    }


    public void verifyStoreOwnerError(String storeID, String memberID) throws Exception {
        if (!verifyMemberIsSystemManager(memberID) && !verifyStoreOwner(storeID, memberID))
            throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
    }

    public void verifyMangerNominatorError(String nominatorMemberID, String nominatedMemberID, String storeID) throws Exception {
        if (storeManagerRepository.get(storeID, nominatedMemberID).getNominatorMemberId() != nominatorMemberID)
            throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
    }

    public void verifyOwnerNominatorError(String nominatorMemberID, String nominatedMemberID, String storeID) throws Exception {
        if (storeOwnerRepository.get(storeID, nominatedMemberID).getNominatorId() != nominatorMemberID)
            throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
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
        return verifyMemberIsSystemManager(memberID) && storeOwner != null && storeOwner.verifyStoreOwnerIsFounder();
    }

    public void createStoreOwnerWithoutAsk(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        if (verifyStoreOwner(storeId, memberId))
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());

        StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void createStoreManagerWithoutAsk(String memberId, String storeId,
                                   boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId) throws Exception {
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId);
            addNewStoreManagerToTheMarket(newStoreManager);
        } else {
            throw new Exception(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString());
        }
    }


    public void approveInvitationStoreOwner(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId)) {
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());
        }

        StoreOwner newStoreOwner = ownersNominators.get(storeId, memberId);
        if (newStoreOwner==null){
            throw new IllegalArgumentException("the invitation no longer exist");
        }
        ownersNominators.delete(newStoreOwner);
        addNewStoreOwnerToTheMarket(newStoreOwner);
    }

    public void declineInvitationStoreOwner(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId)) {
            throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());
        }

        StoreOwner newStoreOwner = ownersNominators.get(storeId, memberId);
        ownersNominators.delete(newStoreOwner);

    }

    public void approveInvitationStoreManager(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId) || verifyStoreManager(storeId, memberId)) {
            throw new Exception("member already manager or owner in this store");
        }
        StoreManager newStoreManager = managerNominators.get(storeId, memberId);
        if (newStoreManager==null){
            throw new IllegalArgumentException("the invitation no longer exist");
        }
        managerNominators.delete(newStoreManager);
        addNewStoreManagerToTheMarket(newStoreManager);
    }

    public void declineInvitationStoreManager(String memberId, String storeId) throws Exception {
        if (verifyStoreOwner(storeId, memberId) || verifyStoreManager(storeId, memberId)) {
            throw new Exception("member already manager or owner in this store");
        }
        StoreManager newStoreManager = managerNominators.get(storeId, memberId);
        managerNominators.delete(newStoreManager);
        //addNewStoreOwnerToTheMarket(newStoreOwner);
    }


    public void addSystemManger(String memberId) {
        SystemManager systemManager = new SystemManager(memberId);
        synchronized (systemManagers) {
            systemManagers.add(systemManager);
            storeManagerRepository.addSystemManager(memberId);
        }
    }

    public void updateStoreManagerPermissions(String memberId, String storeId,
                                              boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberID) throws Exception {
        if (verifyStoreManager(storeId, memberId)) {
            if (getStoreManager(storeId, memberId).getNominatorMemberId().equals(nominatorMemberID)) {
//                StoreManager storeManager = getStoreManager(storeId, memberId);
//                storeManager.setPermissions(inventoryPermissions, purchasePermissions);
//                storeManager = getStoreManager(storeId, memberId);
                storeManagerRepository.updateStoreManagerPermissions(memberId,storeId,inventoryPermissions,purchasePermissions,nominatorMemberID);
            } else {
                throw new Exception(ExceptionsEnum.notNominatorOfThisEmployee.toString());
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
        storeManagerRepository.save(storeManager);
    }

    private void addNewStoreManagerNominatorToTheMarket(StoreManager storeManager) {
        managerNominators.save(storeManager);
    }

    private void addNewStoreOwnerNominatorToTheMarket(StoreOwner storeOwner) {
        ownersNominators.save(storeOwner);
    }

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner) {
        storeOwnerRepository.save(storeOwner);
    }

    public void addNewStoreManagerToTheMarketForTests(StoreManager storeManager) {
        if(!verifyStoreManager(storeManager.getStore_ID(), storeManager.getMember_ID()))
            storeManagerRepository.save(storeManager);
    }

    public void addNewStoreOwnerToTheMarketForTests(StoreOwner storeOwner) {
        if(!verifyStoreOwner(storeOwner.getStore_ID(), storeOwner.getMember_ID()))
            storeOwnerRepository.save(storeOwner);
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
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdManagers(memberId)) {
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
            for (StoreManager currStoreManager : storeManagerRepository.getAllMemberIdManagers(memberId)) {
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


    public void addManagerNominator(String memberId, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions, String nominatorMemberId) throws Exception {
        StoreManager storeManager = managerNominators.get(memberId,storeId);
        if (storeManager!=null  ){
            throw new IllegalArgumentException("this member already nominated to be store manager in this store");
        }
        if (!verifyStoreOwner(storeId, memberId) && !verifyStoreManager(storeId, memberId)) {
            StoreManager newStoreManager = new StoreManager(memberId, storeId, inventoryPermissions, purchasePermissions, nominatorMemberId);
            addNewStoreManagerNominatorToTheMarket(newStoreManager);
        } else {
            throw new Exception(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString());
        }
    }


    public void addOwnerNominator(String memberId, String storeId, boolean founder, String nominatorMemberId) throws Exception {
        StoreOwner storeOwner = ownersNominators.get(memberId,storeId);
        if (storeOwner!=null  ) {
            throw new IllegalArgumentException("this member already nominated to be store owner in this store");
        }
        if (verifyStoreOwner(storeId, memberId))
                throw new Exception(ExceptionsEnum.memberIsAlreadyStoreOwner.toString());

        StoreOwner newStoreOwner = new StoreOwner(memberId, storeId, founder, nominatorMemberId);
        addNewStoreOwnerNominatorToTheMarket(newStoreOwner);
    }

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

    public void fireStoreOwner(String memberIdToFire, String storeID){
        List<String> ownersOfTheStore = getAllStoreOwners(storeID);
        for(int i=0 ; i<ownersOfTheStore.size() ; i++){
            String storeOwner = ownersOfTheStore.get(i);
            if(storeOwnerRepository.get(storeID, storeOwner).getNominatorId().equals(memberIdToFire)){
                fireStoreOwner(storeOwner, storeID);
            }
        }
        List<String> managersOfTheStore = getAllStoreManagers(storeID);
        for(int i=0 ; i<managersOfTheStore.size() ; i++){
            String storeManager = managersOfTheStore.get(i);
            if(storeManagerRepository.get(storeID, storeManager).getNominatorId().equals(memberIdToFire)){
                fireStoreManager(storeManager, storeID);
            }
        }
        storeOwnerRepository.delete(storeOwnerRepository.get(storeID, memberIdToFire));
    }

    public void fireStoreManager(String memberIdToFire, String storeID){
        storeManagerRepository.delete(storeManagerRepository.get(storeID, memberIdToFire));
    }
}

