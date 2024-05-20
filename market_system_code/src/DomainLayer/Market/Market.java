package DomainLayer.Market;

import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;

public class Market {
    private StoreFacade storeFacade = StoreFacade.getInstance();
    private UserFacade userFacade = UserFacade.getInstance();
    private RoleFacade roleFacade = RoleFacade.getInstance();

    public void addProductToStore(String username, int storeID, String productName, int price, int quantity){
        if (roleFacade.verifyStoreOwner(storeID, username)){
            storeFacade.addProductToStore(storeID, productName, price, quantity);
        }
        else {
            throw new IllegalArgumentException("the user is not store owner in the specific store so he cannot add an item");
        }
    }
}
