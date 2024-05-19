package DomainLayer.Role;

import java.util.ArrayList;
import java.util.List;

public class RoleFacade {

    private static RoleFacade roleFacadeInstance;
    private List<StoreOwner> storeOwnersList;  //todo: move this to repository

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
