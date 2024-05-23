package DomainLayer.Market;

import DomainLayer.AuthorizationsAndSecurity.AuthorizationAndSecurityFacade;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Map;


public class Market {
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    //private Set<Integer> systemManagerIds;
    private AuthorizationAndSecurityFacade authorizationAndSecurityFacade;
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;
    private boolean initialized= false;
    Object initializedLock;

    public synchronized static Market getInstance() {
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
        this.authorizationAndSecurityFacade = AuthorizationAndSecurityFacade.getInstance();
        supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
    }

    public void init(String userName, String password, int licensedDealerNumber,
                     String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address){
        synchronized (initializedLock) {
            if (initialized == true) {
                return;
            }
        }

        // userFacade.register(username, password)
       // int systemMangerId = userFacade.getByUserName();
       // systemManagerIds.add(systemMangerId);
        paymentServicesFacade.addExternalService(licensedDealerNumber,paymentServiceName,url);
        supplyServicesFacade.addExternalService(licensedDealerNumber1,supplyServiceName, address);
        synchronized (initializedLock) {
            initialized = true;
        }
    }

    public boolean payWithExternalPaymentService() {
        //HashMap<Integer, Integer>  productIdAndAmount= userFacade.payWithExternalPaymentService(userId);
        return true;
    }

    public void logout(int memberID){
        //todo add condition if the user is logged in
        userFacade.getUserByID(memberID).Logout();
        authorizationAndSecurityFacade.removeToken(memberID);
    }


    public void exitMarketSystem(int userID){
        userFacade.exitMarketSystem(userID);
    }


    public void enterMarketSystem(){
        userFacade.addUser();
    }

    public void register( int userId,String username, String password, String birthday, String address) throws Exception {
        //check validation
        String encryptedPassword = authorizationAndSecurityFacade.encodePassword(password);
        userFacade.register(userId, username,encryptedPassword,birthday,address);
        authorizationAndSecurityFacade.generateToken(userId);
    }

    public void Login(int userID,String username, String password) throws Exception {
        String encryptedPassword = authorizationAndSecurityFacade.encodePassword(password);
        userFacade.Login(userID, username,encryptedPassword);
        authorizationAndSecurityFacade.generateToken(userID);
    }

    public void addProductToBasket(String productName, int quantity, int storeId, int userId) throws Exception
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        boolean canAddToBasket = storeFacade.checkQuantityAndPolicies(productName, quantity, storeId, userId);
        if (canAddToBasket)
        {
            int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
            userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        }
        else
        {
            throw new IllegalArgumentException("The product you try to add doesn't meet the store policies");
        }
    }

    public void removeProductFromBasket(String productName, int storeId, int userId)throws Exception
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        boolean canRemoveFromBasket = userFacade.checkIfCanRemove(productName, storeId, userId);
        if (canRemoveFromBasket)
        {
            userFacade.removeItemFromUserCart(productName, storeId, userId);
        }
        else
        {
            throw new IllegalArgumentException("The product you try to remove is not in the basket");
        }
    }


    public void openStore(int user_ID)throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        if (userFacade.isUserLoggedIn(user_ID)) {
            int store_ID = this.storeFacade.openStore();   //todo: compare to use case parameters
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            this.roleFacade.createStoreOwner(member_ID, store_ID, true);
        } else {
            throw new IllegalArgumentException("The user is not logged in so he cannot open a store");
        }
    }

    public void addProductToStore(int memberID, int storeID, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(memberID));
        if (!succeeded){
            logout(memberID);
            throw new Exception("your session was over please log in again");
        }
        if (roleFacade.verifyStoreOwner(storeID, memberID) ||
                (roleFacade.verifyStoreManager(storeID, memberID) &&
                        roleFacade.managerHasInventoryPermissions(memberID, storeID))) {
            storeFacade.addProductToStore(storeID, productName, price, quantity, description, categoryStr);
        } else {
            throw new Exception("User has no inventory permissions");
        }
    }

    public void removeProductFromStore(int memberID, int storeID, String productName) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(memberID));
        if (!succeeded){
            logout(memberID);
            throw new Exception("your session was over please log in again");
        }
        if (roleFacade.verifyStoreOwner(storeID, memberID) ||
                (roleFacade.verifyStoreManager(storeID, memberID) &&
                        roleFacade.managerHasInventoryPermissions(memberID, storeID))) {
            storeFacade.removeProductFromStore(storeID, productName);
        } else {
            throw new Exception("User has no inventory permissions");
        }
    }

    public void updateProductInStore(int memberID, int storeID, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(memberID));
        if (!succeeded){
            logout(memberID);
            throw new Exception("your session was over please log in again");
        }
        if (roleFacade.verifyStoreOwner(storeID, memberID) ||
                (roleFacade.verifyStoreManager(storeID, memberID) &&
                        roleFacade.managerHasInventoryPermissions(memberID, storeID))) {
            storeFacade.updateProductInStore(storeID, productName, price, quantity, description, categoryStr);
        } else {
            throw new Exception("User has no inventory permissions");
        }
    }

    public void appointStoreOwner(int firstMemberID, int secondMemberID, int storeID) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(firstMemberID));
        if (!succeeded){
            logout(firstMemberID);
            throw new Exception("your session was over please log in again");
        }
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

    public void appointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(firstMemberID));
        if (!succeeded){
            logout(firstMemberID);
            throw new Exception("your session was over please log in again");
        }
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

    public void updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(firstMemberID));
        if (!succeeded){
            logout(firstMemberID);
            throw new Exception("your session was over please log in again");
        }
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
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (roleFacade.verifyStoreOwner(store_ID, member_ID) && roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID)) {
                if (storeFacade.verifyStoreExist(store_ID)) {
                    storeFacade.closeStore(store_ID);
                    List<Integer> storeManagers = roleFacade.getAllStoreManagers(store_ID);
                    List<Integer> storeOwners = roleFacade.getAllStoreOwners(store_ID);
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

    public Map<Integer, String> getInformationAboutRolesInStore(int user_ID, int store_ID) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        Map<Integer, String> information = null;

        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (roleFacade.verifyStoreOwner(store_ID, member_ID)) {
                if (storeFacade.verifyStoreExist(store_ID)) {
                    information = roleFacade.getInformationAboutStoreRoles(store_ID);
                }else {
                    throw new Exception("Store does not exist");
                }
            } else {
                throw new IllegalArgumentException("Only store owner get information about his store workers");
            }
        } else{
            throw new IllegalArgumentException("User is not logged in, so he get information about the roles int his store");
        }
        return information;
    }

    public Map<Integer, List<Integer>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        Map<Integer, List<Integer>> managersAuthorizations;

        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (roleFacade.verifyStoreOwner(store_ID, member_ID)) {
                if (storeFacade.verifyStoreExist(store_ID)) {
                    managersAuthorizations = roleFacade.getStoreManagersAuthorizations(store_ID);
                }else {
                    throw new Exception("Store does not exist");
                }
            } else {
                throw new IllegalArgumentException("Only store owner can get authorizations of his store managers");
            }
        } else{
            throw new IllegalArgumentException("User is not logged in, so he can't get the authorizations of his store managers");
        }
        return managersAuthorizations;

    }

    public List<Integer> getInformationAboutStores(int user_ID)throws Exception
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        List<Integer> openedStores = storeFacade.getInformationAboutOpenStores(); // open stores available for everyone
        List<Integer> closedStores = storeFacade.getInformationAboutClosedStores(); //closed stores available only for owners/ system managers
        List<Integer> closedStoreAvailable = null;

        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (!this.roleFacade.verifyMemberIsSystemManager(user_ID))
                closedStoreAvailable = roleFacade.getStoresByOwner(closedStores, member_ID);
            else
                closedStoreAvailable = closedStores;
        }

        List<Integer> allAvailableStores = new ArrayList<>(openedStores);
        if (closedStoreAvailable != null) {
            allAvailableStores.addAll(closedStoreAvailable);
        }

        return allAvailableStores;
    }

    public List<String> getInformationAboutProductInStore(int user_ID, int store_ID) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        List<String> storeProducts = null;
        if (storeFacade.verifyStoreExist(store_ID)) {
            storeProducts = storeFacade.getStoreProducts(store_ID);
        }else {
            throw new Exception("Store does not exist");
        }
        return storeProducts;
    }

    public void modifyShoppingCart(String productName, int quantity, int storeId, int userId)throws Exception
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        if (quantity == 0)
            removeProductFromBasket(productName, storeId, userId);
        else
        {
            boolean canModify = storeFacade.checkQuantityAndPolicies(productName, quantity, storeId, userId);
            if (canModify)
            {
                int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
                userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
            }
            else
            {
                throw new IllegalArgumentException("The product you try to add doesn't meet the store policies");
            }
        }
    }

    public Map<Integer, Integer> marketManagerAskInfo(int user_ID)throws Exception
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        if (userFacade.isUserLoggedIn(user_ID)) {
            if (this.roleFacade.verifyMemberIsSystemManager(user_ID))
            {
                return paymentServicesFacade.getStorePurchaseInfo(); //returns StoreId and amount of purchases in the store
            }
            else
            {
                throw new IllegalArgumentException("You are not the system manager, so you can do this action.");
            }
        }
        else
        {
            throw new IllegalArgumentException("You are not logged in.");
        }
    }

    public Map<Integer, Integer> storeOwnerGetInfoAboutStore(int user_ID, int store_ID) throws Exception //return receiptId and total amount in the receipt for the specific store
    {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        Map<Integer, Integer> storeReceiptsAndTotalAmount = new HashMap<>();

        if (userFacade.isUserLoggedIn(user_ID)) {
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            if (roleFacade.verifyStoreOwner(store_ID, member_ID)) {
                if (storeFacade.verifyStoreExist(store_ID)) {
                    storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount(store_ID);
                }else {
                    throw new Exception("Store does not exist");
                }
            } else {
                throw new IllegalArgumentException("Only store owner can get information of his purchases");
            }
        } else{
            throw new IllegalArgumentException("User is not logged in, so he can't get purchase history");
        }
        if (storeReceiptsAndTotalAmount.isEmpty())
            throw new IllegalArgumentException("There are no purchases in the store");
        return storeReceiptsAndTotalAmount;
    }

    public int checkingCartValidationBeforePurchase(int user_ID) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
        if (!succeeded){
            logout(user_ID);
            throw new Exception("your session was over please log in again");
        }
        int totalPrice = 0;
        if(this.userFacade.isUserCartEmpty(user_ID))  //todo: verify if needed
            throw new Exception("User cart is empty, there's nothing to purchase");
        else {
            List<Integer> stores = this.userFacade.getCartStoresByUser(user_ID);
            for(Integer store_ID: stores)
            {
                Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(user_ID, store_ID);
                int quantity;
                for(String productName: products.keySet()) {
                    quantity = products.get(productName).get(0);
                    if(!this.storeFacade.checkQuantityAndPolicies(productName, quantity, store_ID, user_ID))
                        throw new Exception("Item is not available or policy conditions are not met");
                    else if(!this.supplyServicesFacade.checkAvailableExternalSupplyService(this.userFacade.getUserAddress(user_ID), null))
                        throw new Exception("Unfortunately, there is no shipping for the user address");
                }
                int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
                int storeTotalPrice = this.storeFacade.calculateTotalCartPriceAfterDiscount(store_ID, products, storeTotalPriceBeforeDiscount);
                totalPrice += storeTotalPrice;
            }
        }
        return totalPrice;
    }


    public List<String> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        List<String> filteredProductNames;
        if (categoryStr != null)
            storeFacade.checkCategory(categoryStr);

        if (storeFacade.verifyStoreExist(storeId))
        {
            if (categoryStr != null)
                storeFacade.checkProductExistInStore(productName, storeId);
            filteredProductNames = storeFacade.inStoreProductSearch(productName, categoryStr, keywords, storeId);
        }
        else
            throw new IllegalArgumentException("The store you try to search in doesnt exist.");

        return filteredProductNames;
    }

    public List<String> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<Integer> stores = this.storeFacade.getStores();
        for(Integer store_ID: stores)
        {
            filteredProductNames.addAll(inStoreProductSearch(userId, productName, categoryStr, keywords,store_ID));
        }
        return filteredProductNames;
    }

        public List<String> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating)throws Exception {
            boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded){
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
        List<String> filteredProductNames = null;
        if (minPrice <= maxPrice)
        {
            if(storeMinRating <= 5 && storeMinRating >= 0) {
                if (productMinRating <= 5 && productMinRating >= 0) {
                    if (categoryStr != null)
                        storeFacade.checkCategory(categoryStr);
                    if (storeFacade.verifyStoreExist(storeId)) {
                        filteredProductNames = storeFacade.inStoreProductFilter(categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
                    } else
                        throw new IllegalArgumentException("The store you try to search in doesnt exist.");
                } else
                    throw new IllegalArgumentException("The rating you entered is invalid");
            }
        }
        else
            throw new IllegalArgumentException("The price range you entered is invalid");

        return filteredProductNames;
    }

    public List<String> generalProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        boolean succeeded=  authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
        if (!succeeded){
            logout(userId);
            throw new Exception("your session was over please log in again");
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<Integer> stores = this.storeFacade.getStores();
        for(Integer store_ID: stores)
        {
            filteredProductNames.addAll(inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, store_ID, productsFromSearch, storeMinRating));
        }
        return filteredProductNames;
    }

    public boolean isInitialized() {
        synchronized (initializedLock) {
            return initialized;
        }
    }
}
