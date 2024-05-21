package DomainLayer.Market;

import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class Market {
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    private Set<Integer> systemManagerIds;
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;
    private boolean initialized= false;

    public static Market getInstance() {
        if (MarketInstance == null) {
            MarketInstance = new Market();
        }
        return MarketInstance;
    }



    private Market(){
        this.storeFacade = StoreFacade.getInstance();
         this.userFacade = UserFacade.getInstance();
          this.roleFacade = RoleFacade.getInstance();
          this.paymentServicesFacade = PaymentServicesFacade.getInstance();
    }

    public void init(String userName, String password, int licensedDealerNumber,
                     String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address){
        if(initialized==true){
            return;
        }

        // userFacade.register(username, password)
       // int systemMangerId = userFacade.getByUserName();
       // systemManagerIds.add(systemMangerId);
        paymentServicesFacade.addExternalService(licensedDealerNumber,paymentServiceName,url);
        supplyServicesFacade.addExternalService(licensedDealerNumber1,supplyServiceName, address);

        initialized = true;
    }

    public boolean payWithExternalPaymentService(){
        //HashMap<Integer, Integer>  productIdAndAmount= userFacade.payWithExternalPaymentService(userId);
        return true;
    }





//    public Market(){
//
//    }

    public void Logout(int memberID){
        //todo add condition if the user is logged in
        userFacade.getUserByID(memberID).Logout();
    }
//todo Nitzan fix this
//    public void addProductToStore(int memberID, int storeID, String productName, int price, int quantity) throws Exception {
//        if (roleFacade.verifyStoreOwner(storeID, memberID)) {
//            storeFacade.addProductToStore(storeID, productName, price, quantity);
//        } else {
//            throw new Exception("Only store owner can add product to store");
//        }
//    }

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

    public void removeProductFromStore(int memberID, int storeID, String productName) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, memberID)) {
            storeFacade.removeProductFromStore(storeID, productName);
        } else {
            throw new Exception("Only store owner can remove product from store");
        }
    }

    public void updateProductInStore(int memberID, int storeID, String productName, int price, int quantity) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, memberID)) {
            storeFacade.updateProductInStore(storeID, productName, price, quantity);
        } else {
            throw new Exception("Only store owner can update product in store");
        }
    }

//    public void closeStore(int member_ID, int store_ID) throws Exception
//    {
//        if(roleFacade.verifyStoreOwner(store_ID, member_ID) && roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID))
//        {
//            if(storeFacade.verifyStoreExist(store_ID)) {
//                storeFacade.closeStore(store_ID);
//                List<Integer> storeRoles = roleFacade.getAllStoreRoles(store_ID);
//                //todo: add function which send notification to all store roles (notification component).
//                //todo: update use-case parameters
//            }
//            else {
//                throw new Exception("Store does not exist");
//            }
//        }
//    }
    public void AppointStoreOwner(int firstMemberID, int secondMemberID, int storeID) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, firstMemberID)) {
            if (!roleFacade.verifyStoreOwner(storeID, secondMemberID)) {
                roleFacade.createStoreOwner(secondMemberID, storeID, false);
            } else {
                throw new Exception("Member is already owner of this store");
            }
        } else {
            throw new Exception("Only store owner can appoint new store owner");
        }
    }

    public void AppointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, firstMemberID)) {
            if (!roleFacade.verifyStoreManager(storeID, secondMemberID)) {
                roleFacade.createStoreManager(secondMemberID, storeID, inventoryPermissions, purchasePermissions);
            } else {
                throw new Exception("Member is already manager of this store");
            }
        } else {
            throw new Exception("Only store owner can appoint new store manager");
        }
    }

    public void UpdateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (roleFacade.verifyStoreOwner(storeID, firstMemberID)) {
            if (roleFacade.verifyStoreManager(storeID, secondMemberID)) {
                roleFacade.updateStoreManagerPermissions(secondMemberID, storeID, inventoryPermissions, purchasePermissions);
            } else {
                throw new Exception("Member is not a manager of this store");
            }
        } else {
            throw new Exception("Only store owner can update store manager permissions");
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
