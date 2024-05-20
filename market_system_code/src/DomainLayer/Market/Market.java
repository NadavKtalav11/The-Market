package DomainLayer.Market;

import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;

public class Market {
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;

    public void addProductToStore(int storeName , String username, String itemName , int quantity){
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

}
