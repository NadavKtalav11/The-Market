package DomainLayer.Market;

import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;

import java.util.List;

public class Market {
    private StoreFacade storeFacade;
    private RoleFacade roleFacade;

    Market(){
        this.roleFacade = RoleFacade.getInstance();
        this.storeFacade = StoreFacade.getInstance();
    }

    public void closeStore(int member_ID, int store_ID) throws Exception {
        if(roleFacade.verifyStoreOwner(store_ID, member_ID) && roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID))
        {
            if(storeFacade.verifyStoreExist(store_ID)) {
                storeFacade.closeStore(store_ID);
                List<Integer> storeRoles = roleFacade.getAllStoreRoles(store_ID);
                //todo: add function which send notification to all store roles (notification component).
                //todo: update use-case parameters
            }
            else {
                throw new Exception("Store does not exist");
            }
        }
        else {
            throw new Exception("Only store founder can close a store");
        }
    }

}
