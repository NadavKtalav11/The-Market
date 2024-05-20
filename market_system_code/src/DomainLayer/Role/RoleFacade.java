package DomainLayer.Role;

import java.util.ArrayList;
import java.util.List;

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

    private void addNewStoreOwnerToTheMarket(StoreOwner storeOwner)
    {
        storeOwnersList.add(storeOwner);
    }

}
