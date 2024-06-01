package DomainLayer.Market;

import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.ExceptionsEnum;

import java.util.*;


public class Market {
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    private Set<Integer> systemManagerIds;
    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;
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
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
    }

    public Market newForTests(){
        MarketInstance = new Market();
        StoreFacade storeFacade1 =  storeFacade.newForTest();
        UserFacade userFacade1 =  userFacade.newForTest();
        RoleFacade roleFacade1 =  roleFacade.newForTest();
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade1 =  authenticationAndSecurityFacade.newForTest();
        PaymentServicesFacade paymentServicesFacade1 =  paymentServicesFacade.newForTest();
        SupplyServicesFacade supplyServicesFacade1 = supplyServicesFacade.newForTest();
        storeFacade=storeFacade1;
        userFacade = userFacade1;
        roleFacade = roleFacade1;
        authenticationAndSecurityFacade = authenticationAndSecurityFacade1;
        paymentServicesFacade = paymentServicesFacade1;
        supplyServicesFacade = supplyServicesFacade1;
        return MarketInstance;


    }



    public void init(String userName, String password,String birthday, String country, String city, String address, String name, int licensedDealerNumber,
                     String paymentServiceName, String url,
                     int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities) throws Exception {
        synchronized (initializedLock) {
            if (initialized == true) {
                return;
            }
        }
        try {
            // Check for supply service
            if (supplyServiceName == null || licensedDealerNumber1<0 || countries==null || cities==null ) {
                throw new Exception("The system has not been able to be launched since there is a problem with the supply service details");
            }
            // Check for payment service
            if (paymentServiceName == null || url==null || licensedDealerNumber <0) {
                throw new Exception("The system has not been able to be launched since there is a problem with the payment service details");
            }
            // Initialization logic here
            initialized = true;
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        String encrypted = authenticationAndSecurityFacade.encodePassword(password);
        int firstUserID = enterMarketSystem();
        int systemMangerId = userFacade.register(firstUserID,userName, encrypted, birthday,country,city,address,name);
        //int systemMangerId = userFacade.registerSystemAdmin(userName, encrypted, birthday,country,city,address,name);
        systemManagerIds.add(systemMangerId);
        paymentServicesFacade.addExternalService(licensedDealerNumber,paymentServiceName,url);
        supplyServicesFacade.addExternalService(licensedDealerNumber1,supplyServiceName, countries, cities);
        synchronized (initializedLock) {
            initialized = true;
        }
    }

    public void addExternalPaymentService(int licensedDealerNumber,String paymentServiceName, String url, int systemMangerId) throws Exception {
        try {
            if(!systemManagerIds.contains(systemMangerId)){
                throw new Exception("Only system manager is allowed to add new external payment service");
            }
            if (paymentServiceName == null || licensedDealerNumber<0 || url==null ) {
                throw new Exception("The system has not been able to add the payment service due to invalid details");
            }
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        paymentServicesFacade.addExternalService(licensedDealerNumber, paymentServiceName, url);
    }

    public void removeExternalPaymentService(int licensedDealerNumber, int systemMangerId) throws Exception {
        try {
            if (!systemManagerIds.contains(systemMangerId)) {
                throw new Exception("Only system manager is allowed to remove external payment services");
            }
            if (paymentServicesFacade.getAllPaymentServices().size() <= 1) {
                throw new Exception("There must remain at least one external payment service in the system");
            }
        }
        catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        paymentServicesFacade.removeExternalService(licensedDealerNumber);

    }

    public void addExternalSupplyService(int licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities, int systemManagerId) throws Exception {
        try {
            if (!systemManagerIds.contains(systemManagerId)) {
                throw new Exception("Only system manager is allowed to add new external supply service");
            }
            if (supplyServiceName == null || countries ==null || cities ==null || licensedDealerNumber < 0 || supplyServiceName == null ) {
                throw new Exception("The system has not been able to add the supply service due to invalid details");
            }
        } catch (Exception e) {
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

    }

    public void removeExternalSupplyService(int licensedDealerNumber, int systemManagerId) throws Exception {
        synchronized (initializedLock) {
            try {
                if (!systemManagerIds.contains(systemManagerId)) {
                    throw new Exception("Only system manager is allowed to remove external supply service");
                }
                if (supplyServicesFacade.getAllSupplyServices().size() <= 1) {
                    throw new Exception("There must remain at least one external supply service in the system");
                }
                supplyServicesFacade.removeExternalService(licensedDealerNumber);
            } catch (Exception e) {
                throw e;  // Re-throwing the exception to be handled by the caller
            }
        }
    }

    public Set<Integer> getSystemManagerIds(){
        return systemManagerIds;
    }


    public void payWithExternalPaymentService(int price,String creditCard, int cvv, int month, int year, String holderID, int userId, Map<Integer, Map<String, Integer>> productList) throws Exception{
        if(price<= 0 || month> 12 || month<1 ||year < 2020 ||holderID==null||userId<0 ||productList==null) {
            throw new Exception("There is a problem with the provided payment measure or details of the order.\n");
        }
        if(paymentServicesFacade.getAllPaymentServices().size()<1){
            throw new Exception("There is no available external payment system.\n");
        }
        Map<Integer,Integer> receiptIdStoreId = paymentServicesFacade.pay(price, creditCard, cvv, month, year, holderID, userId, productList); //<receiptId, storeId>
        //print when implement notifications (purchase successes)

        //Add the receiptId and storeId to the user receipts map
        if (userFacade.isMember(userId))
        {
            userFacade.addReceiptToUser(receiptIdStoreId, userId);
        }
        //Add the receiptId and userId to the store receipts map
        for (Integer receiptId : receiptIdStoreId.keySet()) {
            storeFacade.addReceiptToStore(receiptIdStoreId.get(receiptId), receiptId, userId);
        }
    }

    public void paymentFailed(int user_ID) throws Exception {
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
        authenticationAndSecurityFacade.removeToken(userId);
    }


    public void exitMarketSystem(int userID){
        userFacade.exitMarketSystem(userID);
    }


    public int enterMarketSystem(){
        return userFacade.addUser();
    }

    public void register( int userId,String username, String password, String birthday,String country, String city, String address, String name) throws Exception {
        //check password validation
        if (password == null || password.equals("")){
            throw new Exception("All fields are required.");
        }
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        userFacade.register(userId, username,encryptedPassword,birthday,country,city,address,name);
        authenticationAndSecurityFacade.generateToken(userId);
    }

    public void Login(int userID,String username, String password) throws Exception {
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        userFacade.Login(userID, username,encryptedPassword);
        authenticationAndSecurityFacade.generateToken(userID);
    }

    public void addProductToBasket(String productName, int quantity, int storeId, int userId) throws Exception {
        if (userFacade.isMember(userId)){
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
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
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
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


    public void openStore(int user_ID, String name, String description)throws Exception {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.isUserLoggedInError(user_ID);
        if(name == null || name.equals("")) {
            throw new IllegalArgumentException(ExceptionsEnum.illegalStoreName.toString());
        }
        int store_ID = this.storeFacade.openStore(name, description);
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        this.roleFacade.createStoreOwner(member_ID, store_ID, true, -1);
    }

    public void addProductToStore(int userId, int storeId, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        if (userFacade.getUserByID(userId) != null){
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null){
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.addProductToStore(storeId, productName, price, quantity, description, categoryStr);
                    } else {
                        throw new Exception("User has no inventory permissions");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no inventory permissions");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void removeProductFromStore(int userId, int storeId, String productName) throws Exception {
        if (userFacade.getUserByID(userId) != null){
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.removeProductFromStore(storeId, productName);
                    } else {
                        throw new Exception("User has no inventory permissions");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no inventory permissions");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void updateProductInStore(int userId, int storeId, String productName, int price, int quantity,
                                                        String description, String categoryStr) throws Exception {
        if (userFacade.getUserByID(userId) != null) {
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.updateProductInStore(storeId, productName, price, quantity, description, categoryStr);
                    } else {
                        throw new Exception("User has no inventory permissions");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no inventory permissions");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void appointStoreOwner(int nominatorUserId, String nominatedUsername, int storeId) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.createStoreOwner(nominatedMemberID, storeId, false, nominatorMemberID);
                        } else {
                            throw new Exception("Username was not found");
                        }
                    } else {
                        throw new Exception("User has no permission to appoint store owner");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no permission to appoint store owner");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void appointStoreManager(int nominatorUserId, String nominatedUsername, int storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.createStoreManager(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
                        } else {
                            throw new Exception("Username was not found");
                        }
                    } else {
                        throw new Exception("User has no permission to appoint store manager");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no permission to appoint store manager");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void updateStoreManagerPermissions(int nominatorUserId, String nominatedUsername, int storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                int nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            int nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.updateStoreManagerPermissions(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
                        } else {
                            throw new Exception("Username was not found");
                        }
                    } else {
                        throw new Exception("User has no permission to update store manager permissions");
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception("User has no permission to update store manager permissions");
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void closeStore(int user_ID, int store_ID) throws Exception
    {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.isUserLoggedInError(user_ID);
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        if (roleFacade.verifyStoreOwner(store_ID, member_ID) && roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID)) {
            if (storeFacade.verifyStoreExist(store_ID)) {
                storeFacade.closeStore(store_ID);
                List<Integer> storeManagers = roleFacade.getAllStoreManagers(store_ID);
                List<Integer> storeOwners = roleFacade.getAllStoreOwners(store_ID);
                //todo: add function which send notification to all store roles (notification component).
                //todo: update use-case parameters
            } else {
                throw new Exception(ExceptionsEnum.storeNotExist.toString());
            }
        } else {
            throw new Exception("Only store founder can close a store");
        }
    }

    public Map<Integer, String> getInformationAboutRolesInStore(int user_ID, int store_ID) throws Exception {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<Integer, String> information = null;

        userFacade.isUserLoggedInError(user_ID);
        this.verifyUserGetEmployeeInfoIsStoreOwner(user_ID, store_ID);
        storeFacade.verifyStoreExistError(store_ID);
        information = roleFacade.getInformationAboutStoreRoles(store_ID);

        return information;
    }

    public void verifyUserGetEmployeeInfoIsStoreOwner(int user_ID, int store_ID){
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        if (!roleFacade.verifyStoreOwner(store_ID, member_ID))
            throw new IllegalArgumentException(ExceptionsEnum.userIsNotStoreOwnerSoCantGetEmployeeInfo.toString());
    }

    public Map<Integer, List<Integer>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID) throws Exception {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<Integer, List<Integer>> managersAuthorizations;

        userFacade.isUserLoggedInError(user_ID);
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        if (roleFacade.verifyStoreOwner(store_ID, member_ID)) {
            if (storeFacade.verifyStoreExist(store_ID)) {
                managersAuthorizations = roleFacade.getStoreManagersAuthorizations(store_ID);
            }else {
                throw new Exception(ExceptionsEnum.storeNotExist.toString());
            }
        } else {
            throw new IllegalArgumentException("Only store owner can get authorizations of his store managers");
        }
        return managersAuthorizations;

    }


   // public List<Integer> getInformationAboutStores(int user_ID)throws Exception

    public Map<Integer, Map<String, Integer>> getPurchaseList(int userId)throws Exception{
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
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
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<Integer> openedStores = storeFacade.getInformationAboutOpenStores(); // open stores available for everyone
        List<Integer> closedStores = storeFacade.getInformationAboutClosedStores(); //closed stores available only for owners/ system managers
        List<Integer> closedStoreAvailable = null;

        userFacade.isUserLoggedInError(user_ID);
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        if (!this.roleFacade.verifyMemberIsSystemManager(user_ID))
            closedStoreAvailable = roleFacade.getStoresByOwner(closedStores, member_ID);
        else
            closedStoreAvailable = closedStores;

        List<Integer> allAvailableStores = new ArrayList<>(openedStores);
        if (closedStoreAvailable != null) {
            allAvailableStores.addAll(closedStoreAvailable);
        }

        return allAvailableStores;
    }

    public void modifyShoppingCart(String productName, int quantity, int storeId, int userId)throws Exception
    {
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
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
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.isUserLoggedInError(user_ID);
        if (this.roleFacade.verifyMemberIsSystemManager(user_ID))
        {
            return paymentServicesFacade.getStorePurchaseInfo(); //returns StoreId and amount of purchases in the store
        }
        else
        {
            throw new IllegalArgumentException("You are not the system manager, so you can do this action.");
        }

    }



    public Map<Integer, Integer> storeOwnerGetInfoAboutStore(int user_ID, int store_ID) throws Exception //return receiptId and total amount in the receipt for the specific store
    {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<Integer, Integer> storeReceiptsAndTotalAmount = new HashMap<>();

        userFacade.isUserLoggedInError(user_ID);
        int member_ID = this.userFacade.getUsernameByUserID(user_ID);
        if (roleFacade.verifyStoreOwner(store_ID, member_ID)) {
            if (storeFacade.verifyStoreExist(store_ID)) {
                storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount(store_ID);
            }else {
                throw new Exception(ExceptionsEnum.storeNotExist.toString());
            }
        } else {
            throw new IllegalArgumentException("Only store owner can get information of his purchases");
        }
        if (storeReceiptsAndTotalAmount.isEmpty())
            throw new IllegalArgumentException("There are no purchases in the store");
        return storeReceiptsAndTotalAmount;
    }

    public int checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address) throws Exception {
        if (userFacade.isMember(user_ID)) {
            int memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        int totalPrice = 0;
        this.userFacade.isUserCartEmpty(user_ID);

        List<Integer> stores = this.userFacade.getCartStoresByUser(user_ID);
        for(Integer store_ID: stores)
        {
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
            int quantity;
            for(String productName: products.keySet()) {
                quantity = products.get(productName).get(0);
                this.storeFacade.checkQuantityAndPolicies(productName, quantity, store_ID, user_ID);
                int availableExternalSupplyService = this.checkAvailableExternalSupplyService(country, city);
                this.createShiftingDetails(country, city, availableExternalSupplyService, address, user_ID);
            }
            //remove items from stock
            removeUserCartFromStock(user_ID);
            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            int storeTotalPrice = this.storeFacade.calculateTotalCartPriceAfterDiscount(store_ID, products, storeTotalPriceBeforeDiscount);
            totalPrice += storeTotalPrice;
        }

        return totalPrice;
    }

    public int checkAvailableExternalSupplyService(String country, String city) throws Exception {
        int availibleExteranlSupplyService =this.supplyServicesFacade.checkAvailableExternalSupplyService(country,city);
        if(-1==availibleExteranlSupplyService)
            throw new Exception(ExceptionsEnum.ExternalSupplyServiceIsNotAvailable.toString());
        return availibleExteranlSupplyService;
    }

    public void createShiftingDetails(String country, String city, int availibleExteranlSupplyService, String address, int user_ID) throws Exception
    {
        String userName = this.userFacade.getUserByID(user_ID).getName();
        if(!supplyServicesFacade.createShiftingDetails(availibleExteranlSupplyService, userName,country,city,address)){
            throw new Exception(ExceptionsEnum.createShiftingError.toString());
        }
    }

    public void removeUserCartFromStock(int userId) throws Exception {
        if (userFacade.getUserByID(userId) == null) {
            throw new Exception("User not found.");
        }
        if (userFacade.getUserByID(userId).getCart() == null || userFacade.getUserByID(userId).getCart().isCartEmpty()) {
            throw new Exception("User cart is empty, there's nothing to remove from stock.");
        }
        List<Integer> storeIds = userFacade.getUserByID(userId).getCart().getCartStores();
        for (int storeId : storeIds) {
            Map<String, Integer> products = userFacade.getUserByID(userId).getCart().getProductsQuantityByStore(storeId);
            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                for (int i = 0; i < quantity; i++) {
                    storeFacade.removeProductFromStore(storeId, productName);
                }
            }
        }
    }


    public List<String> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId) throws Exception {
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        if (categoryStr != null && !storeFacade.checkCategory(categoryStr))
            throw new IllegalArgumentException("The category you entered in invalid");

        if (storeFacade.verifyStoreExist(storeId))
        {
                if (productName == null || storeFacade.checkProductExistInStore(productName, storeId))
                    return storeFacade.inStoreProductSearch(productName, categoryStr, keywords, storeId);
                else
                    throw new IllegalArgumentException("The product doesn't exist in the store");
        }
        else
            throw new IllegalArgumentException("The store you try to search in doesnt exist.");
    }

    public List<String> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<Integer> stores = this.storeFacade.getStores();
        for(Integer store_ID: stores)
        {
            try {
                filteredProductNames.addAll(inStoreProductSearch(userId, productName, categoryStr, keywords,store_ID));
            }
            catch (Exception e)
            {
                continue;
            }
        }
        if (filteredProductNames.isEmpty())
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInMarket.toString());

        return filteredProductNames;
    }

    public List<String> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating)throws Exception {
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<String> filteredProductNames = null;
        if ((minPrice == null || maxPrice == null) || (minPrice != null && maxPrice != null && minPrice <= maxPrice))
        {
            if(storeMinRating <= 5 && storeMinRating >= 0) {
                if (productMinRating <= 5 && productMinRating >= 0) {
                    if (categoryStr != null && storeFacade.checkCategory(categoryStr))
                        throw new IllegalArgumentException("The category you entered is invalid.");
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

    public List<String> generalProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        if (userFacade.isMember(userId)) {
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
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

    public void tokensChecking(int userId) throws Exception{
        if (userFacade.isMember(userId)){
            int memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }
    }
}
