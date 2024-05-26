package DomainLayer.Market;

import DomainLayer.AuthorizationsAndSecurity.AuthorizationAndSecurityFacade;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;

import javax.swing.event.ListDataEvent;
import java.util.*;


public class Market {
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    private Set<Integer> systemManagerIds;
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
        this.systemManagerIds = new HashSet<>();
    }

    public void init(String userName, String password,String birthday, String country, String city, String address, String name, int licensedDealerNumber,
                     String paymentServiceName, String url,
                     int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities) throws Exception {
        synchronized (initializedLock) {
            if (initialized == true) {
                return;
            }
        }

        int systemMangerId = userFacade.registerSystemAdmin(userName, password, birthday,country,city,address,name);
        systemManagerIds.add(systemMangerId);
        paymentServicesFacade.addExternalService(licensedDealerNumber,paymentServiceName,url);
        supplyServicesFacade.addExternalService(licensedDealerNumber1,supplyServiceName, countries, cities);
        synchronized (initializedLock) {
            initialized = true;
        }
    }

    public void payWithExternalPaymentService(int price,int creditCard, int cvv, int month, int year, String holderID, int userId, Map<Integer, Map<String, Integer>> productList) {
        Map<Integer,Integer> receiptIdStoreId = paymentServicesFacade.pay(price, creditCard, cvv, month, year, holderID, userId, productList); //<receiptId, storeId>
        //print notification (purchase successes) - later
        //Add the receiptId and storeId to the user receipts map
        userFacade.addReceiptToUser(receiptIdStoreId, userId);
        //Add the receiptId and userId to the store receipts map
        for (Integer receiptId : receiptIdStoreId.keySet()) {
            storeFacade.addReceiptToStore(receiptIdStoreId.get(receiptId), receiptId, userId);
        }
    }

    public void paymentFailed(int user_ID) throws Exception {
        List<Integer> stores = this.userFacade.getCartStoresByUser(user_ID);
        returnStock(getPurchaseList(user_ID));
    }

    public void returnStock(Map<Integer, Map<String, Integer>> products){
        for (Integer storeId: products.keySet()){
            storeFacade.returnProductToStore(products.get(storeId), storeId);
        }
    }

    public void logout(int userId){
        //todo add condition if the user is logged in
        userFacade.getUserByID(userId).Logout();
        authorizationAndSecurityFacade.removeToken(userId);
    }


    public void exitMarketSystem(int userID){
        userFacade.exitMarketSystem(userID);
    }


    public void enterMarketSystem(){
        userFacade.addUser();
    }

    public void register( int userId,String username, String password, String birthday,String country, String city, String address, String name) throws Exception {
        //check validation
        String encryptedPassword = authorizationAndSecurityFacade.encodePassword(password);
        userFacade.register(userId, username,encryptedPassword,birthday,country,city,address,name);
        authorizationAndSecurityFacade.generateToken(userId);
    }

    public void Login(int userID,String username, String password) throws Exception {
        String encryptedPassword = authorizationAndSecurityFacade.encodePassword(password);
        userFacade.Login(userID, username,encryptedPassword);
        authorizationAndSecurityFacade.generateToken(userID);
    }

    public void addProductToBasket(String productName, int quantity, int storeId, int userId) throws Exception {
        if (userFacade.isMember(userId)){
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
        }
        if (userFacade.isUserLoggedIn(user_ID)) {
            int store_ID = this.storeFacade.openStore();   //todo: compare to use case parameters
            int member_ID = this.userFacade.getUsernameByUserID(user_ID);
            this.roleFacade.createStoreOwner(member_ID, store_ID, true, -1);
        } else {
            throw new IllegalArgumentException("The user is not logged in so he cannot open a store");
        }
    }

    public void addProductToStore(int userId, int storeID, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
            int memberId = userFacade.getMemberIdByUserId(userId);
            if (roleFacade.verifyStoreOwner(storeID, memberId) ||
                    (roleFacade.verifyStoreManager(storeID, memberId) &&
                            roleFacade.managerHasInventoryPermissions(memberId, storeID))) {
                storeFacade.addProductToStore(storeID, productName, price, quantity, description, categoryStr);
            } else {
                throw new Exception("User has no inventory permissions");
            }
        }
    }

    public void removeProductFromStore(int userId, int storeID, String productName) throws Exception {
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
            int memberId = userFacade.getMemberIdByUserId(userId);
            if (roleFacade.verifyStoreOwner(storeID, memberId) ||
                    (roleFacade.verifyStoreManager(storeID, memberId) &&
                            roleFacade.managerHasInventoryPermissions(memberId, storeID))) {
                storeFacade.removeProductFromStore(storeID, productName);
            } else {
                throw new Exception("User has no inventory permissions");
            }
        }
    }

    public void updateProductInStore(int userId, int storeID, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
            int memberId = userFacade.getMemberIdByUserId(userId);
            if (roleFacade.verifyStoreOwner(storeID, memberId) ||
                    (roleFacade.verifyStoreManager(storeID, memberId) &&
                            roleFacade.managerHasInventoryPermissions(memberId, storeID))) {
                storeFacade.updateProductInStore(storeID, productName, price, quantity, description, categoryStr);
            } else {
                throw new Exception("User has no inventory permissions");
            }
        }
    }

    public void appointStoreOwner(int nominatorUserId, String nominatedUsername, int storeID) throws Exception {
        if (userFacade.isMember(nominatorUserId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(nominatorUserId));
            if (!succeeded) {
                logout(nominatorUserId);
                throw new Exception("your session was over please log in again");
            }
            int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
            if (roleFacade.verifyStoreOwner(storeID, nominatorMemberID)) {
                if(userFacade.getMemberByUsername(nominatedUsername) != null){
                    int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                    if (!roleFacade.verifyStoreOwner(storeID, nominatedMemberID)) {
                        roleFacade.createStoreOwner(nominatedMemberID, storeID, false, nominatorMemberID);
                    } else {
                        throw new Exception("Member is already owner of this store");
                    }
                } else {
                    throw new Exception("Guest appointment is not possible");
                }
            } else {
                throw new Exception("Only store owner can appoint new store owner");
            }
        }
    }

    public void appointStoreManager(int nominatorUserId, String nominatedUsername, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.isMember(nominatorUserId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(nominatorUserId));
            if (!succeeded) {
                logout(nominatorUserId);
                throw new Exception("your session was over please log in again");
            }
            int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
            if (roleFacade.verifyStoreOwner(storeID, nominatorMemberID)) {
                if(userFacade.getMemberByUsername(nominatedUsername) != null){
                    int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                    if (!roleFacade.verifyStoreOwner(storeID, nominatedMemberID) && !roleFacade.verifyStoreManager(storeID, nominatedMemberID)) {
                        roleFacade.createStoreManager(nominatedMemberID, storeID,
                                    inventoryPermissions, purchasePermissions, nominatorMemberID);
                    } else {
                        throw new Exception("Member already has a role in this store");
                    }
                } else {
                    throw new Exception("Guest appointment is not possible");
                }
            } else {
                throw new Exception("Only store owner can appoint new store manager");
            }
        }
    }

    public void updateStoreManagerPermissions(int nominatorUserId, String nominatedUsername, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.isMember(nominatorUserId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(nominatorUserId));
            if (!succeeded) {
                logout(nominatorUserId);
                throw new Exception("your session was over please log in again");
            }
            int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
            if (roleFacade.verifyStoreOwner(storeID, nominatorMemberID)) {
                if(userFacade.getMemberByUsername(nominatedUsername) != null){
                    int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                    if (roleFacade.verifyStoreManager(storeID, nominatedMemberID)) {
                        if(roleFacade.getStoreManager(storeID, nominatedMemberID).getNominatorMemberId() == nominatorMemberID){
                            roleFacade.updateStoreManagerPermissions(nominatedMemberID, storeID, inventoryPermissions, purchasePermissions);
                        } else {
                            throw new Exception("Store owner is not the store manager's nominator");
                        }
                    } else {
                        throw new Exception("User is not a manager of this store");
                    }
                } else {
                    throw new Exception("User is not a manager of this store");
                }
            } else {
                throw new Exception("Only store owner can update store manager permissions");
            }
        }
    }

    public void closeStore(int user_ID, int store_ID) throws Exception
    {
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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


   // public List<Integer> getInformationAboutStores(int user_ID)throws Exception

    public Map<Integer, Map<String, Integer>> getPurchaseList(int userId)throws Exception{
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
        }
        Map<Integer, Map<String, Integer>> purchaseList = new HashMap<>();
        List<Integer> usersStores = userFacade.getCartStoresByUser(userId);
        for (Integer storeId: usersStores) {
            Map<String, Integer> productAndQuantity = new HashMap<>();
            purchaseList.put(storeId, productAndQuantity) ;
            Map <String, List<Integer>> returnedMap = userFacade.getCartProductsByStoreAndUser(storeId , userId);
            for (String productName : returnedMap.keySet()){
                productAndQuantity.put(productName,returnedMap.get(productName).get(0) );
            }

        }
        return purchaseList;
    }

    public List<Integer> getInformationAboutStores(int user_ID) throws Exception {
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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

    public int checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address) throws Exception {
        if (userFacade.isMember(user_ID)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(user_ID));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception("your session was over please log in again");
            }
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
                String userName = this.userFacade.getUserByID(user_ID).getName();
                for(String productName: products.keySet()) {
                    quantity = products.get(productName).get(0);
                    if(!this.storeFacade.checkQuantityAndPolicies(productName, quantity, store_ID, user_ID))
                        throw new Exception("Item is not available or policy conditions are not met");
                    //todo remove comment after david
                    int availibleExteranlSupplyService =this.supplyServicesFacade.checkAvailableExternalSupplyService(country,city);
                    if(-1==availibleExteranlSupplyService)
                        throw new Exception("Unfortunately, there is no shipping for the user address");
                    //todo remove items from stock
                    if(!supplyServicesFacade.createShiftingDetails(availibleExteranlSupplyService, userName,country,city,address)){
                        throw new Exception("Unfortunately, there was problem in creating the shifting");
                    }
                }
                int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
                int storeTotalPrice = this.storeFacade.calculateTotalCartPriceAfterDiscount(store_ID, products, storeTotalPriceBeforeDiscount);
                totalPrice += storeTotalPrice;
            }
        }
        return totalPrice;
    }


    public List<String> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId) throws Exception {
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
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
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<Integer> stores = this.storeFacade.getStores();
        for(Integer store_ID: stores)
        {
            filteredProductNames.addAll(inStoreProductSearch(userId, productName, categoryStr, keywords,store_ID));
        }
        return filteredProductNames;
    }

    public List<String> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating)throws Exception {
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
        }
        List<String> filteredProductNames = null;
        if ((minPrice == null || maxPrice == null) || (minPrice != null && maxPrice != null && minPrice <= maxPrice))
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
        if (userFacade.isMember(userId)) {
            boolean succeeded = authorizationAndSecurityFacade.validateToken(authorizationAndSecurityFacade.getToken(userId));
            if (!succeeded) {
                logout(userId);
                throw new Exception("your session was over please log in again");
            }
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
