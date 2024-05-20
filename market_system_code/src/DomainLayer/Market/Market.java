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
      this.storeFacade = StoreFacade.getInstance();
      this.userFacade = UserFacade.getInstance();
      this.roleFacade = RoleFacade.getInstance();
    }
  
    public void addProductToStore(int memberID, int storeID, String productName, int price, int quantity) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, memberID)) {
            storeFacade.addProductToStore(storeID, productName, price, quantity);
        } else {
            throw new Exception("Only store owner can add product to store");
        }
    }


    public void addProductToBasket(int productId, int quantity, int storeId, int userId)
    {
        boolean canAddToBasket = storeFacade.checkQuantityAndPolicies(productId, quantity, storeId, userId);
        if (canAddToBasket)
        {
            userFacade.addItemsToBasket(productId, quantity, storeId, userId);
        }
        else
        {
            throw new IllegalArgumentException("The product you try to add doesn't meet the store policies");
        }

    }

    public void openStore(int user_ID) {
        if (userFacade.isUserLoggedIn(user_ID)) {
            int store_ID = this.storeFacade.openStore();   //todo: compare to use case parameters
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            this.roleFacade.createStoreOwner(member_ID, store_ID, true);
        } else {
            throw new IllegalArgumentException("The user is not logged in so he cannot open a store");
        }
    }

    public void removeProductFromStore(String username, int storeID, String productName) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, username)) {
            storeFacade.removeProductFromStore(storeID, productName);
        } else {
            throw new Exception("Only store owner can remove product from store");
        }
    }

    public void updateProductInStore(String username, int storeID, String productName, int price, int quantity) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, username)) {
            storeFacade.updateProductInStore(storeID, productName, price, quantity);
        } else {
            throw new Exception("Only store owner can update product in store");
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
    }
    public void AppointStoreOwner(int firstMemberID, int secondMemberID, int storeID) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, firstMemberID)) {
            if (!roleFacade.verifyStoreOwner(storeID, secondMemberID)) {
                roleFacade.createStoreOwner(secondMemberID, storeID, false);
            } else {
                throw new Exception("Member is already store owner");
            }
        } else {
            throw new Exception("Only store owner can appoint new store owner");
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
        else throw new IllegalArgumentException("User is not logged in, so he cannot close a store");
      
    }
}
