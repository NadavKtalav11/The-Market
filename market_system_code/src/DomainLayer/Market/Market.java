package DomainLayer.Market;

import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;

import java.util.List;

public class Market {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;

    public Market(){
      this.storeFacade = StoreFacade.getInstance();
      this.userFacade = UserFacade.getInstance();
      this.roleFacade = RoleFacade.getInstance();
    }

    public UserFacade getUserFacade(){
        return userFacade;
    }
  
    public void addProductToStore(String username, int storeID, String productName, int price, int quantity) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, username)) {
            storeFacade.addProductToStore(storeID, productName, price, quantity);
        } else {
            throw new Exception("User is not the Store owner");
        }
    }

    public void removeProductFromStore(String username, int storeID, String productName) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, username)) {
            storeFacade.removeProductFromStore(storeID, productName);
        } else {
            throw new Exception("User is not the Store owner");
        }
    }

    public void closeStore(int member_ID, int store_ID) throws Exception 
    {
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
