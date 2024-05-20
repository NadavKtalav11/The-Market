package DomainLayer.Market;

import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;

import java.util.List;

public class Market {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;

    Market(){
        this.roleFacade = RoleFacade.getInstance();
        this.storeFacade = StoreFacade.getInstance();
    }
  
    public void addProductToStore(int storeName , String username, String itemName , int quantity)
    {
        int userId = userFacade.getUserID(username);
        int storeId = StoreFacade.getStoreId(storeName);
        boolean canAdd = roleFacade.verifyStoreOwner(userId, storeId);
        if (canAdd){
            storeFacade.addItemToStore(itemName, quantity);
        }
        else {
            throw new IllegalArgumentException("the user is not store owner in the specific store so he cannot add an item");
        }

    }
    
    public void closeStore(int user_ID, int store_ID) throws Exception
    {
        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (roleFacade.verifyStoreOwner(store_ID, member_ID) && roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID)) {
                if (storeFacade.verifyStoreExist(store_ID)) {
                    storeFacade.closeStore(store_ID);
                    List<Integer> storeRoles = roleFacade.getAllStoreRoles(store_ID);
                    //todo: add function which send notification to all store roles (notification component).
                    //todo: update use-case parameters
                } else {
                    throw new Exception("Store does not exist");
                }
            } else {
                throw new Exception("Only store founder can close a store");
            }
        }
        else throw new IllegalArgumentException("User is not logged in, so he cannot close a store")
      
    }

}
