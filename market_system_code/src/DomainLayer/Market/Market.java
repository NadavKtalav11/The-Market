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

    public void openStore(int user_ID)
    {
        if(userFacade.isUserLoggedIn(user_ID))
        {
            int store_ID = this.storeFacade.openStore();   //todo: compare to use case parameters
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            this.roleFacade.createStoreOwner(member_ID, store_ID, true);
        }
        else {
            throw new IllegalArgumentException("The user is not logged in so he cannot open a store");
        }
    }

}
