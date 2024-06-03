package DomainLayer.Market;

import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.Product;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.CartDTO;
import Util.ExceptionsEnum;
import Util.PaymentDTO;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.*;


public class Market {
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    private Set<String> systemManagerIds;
    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;
    private boolean initialized= false;
    private final Object initializedLock;
    private final Object managersLock;

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
        managersLock = new Object();
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



    public void init(UserDTO user, String password, int licensedDealerNumber,
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
            synchronized (initializedLock) {
                initialized = true;
            }
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        String encrypted = authenticationAndSecurityFacade.encodePassword(password);
        String firstUserID = enterMarketSystem();
        String systemMangerId = userFacade.register(firstUserID, user, encrypted);
        //int systemMangerId = userFacade.registerSystemAdmin(userName, encrypted, birthday,country,city,address,name);
        synchronized (managersLock) {
            systemManagerIds.add(systemMangerId);
        }
        paymentServicesFacade.addExternalService(licensedDealerNumber,paymentServiceName,url);
        supplyServicesFacade.addExternalService(licensedDealerNumber1,supplyServiceName, countries, cities);
        synchronized (initializedLock) {
            initialized = true;
        }
    }

    public void addExternalPaymentService(int licensedDealerNumber,String paymentServiceName, String url, String systemMangerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemMangerId)) {
                    throw new Exception("Only system manager is allowed to add new external payment service");
                }
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

    public void removeExternalPaymentService(int licensedDealerNumber, String systemMangerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemMangerId)) {
                    throw new Exception("Only system manager is allowed to remove external payment services");
                }
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

    public void addExternalSupplyService(int licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities, String systemManagerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemManagerId)) {
                    throw new Exception("Only system manager is allowed to add new external supply service");
                }
            }
            if (supplyServiceName == null || countries ==null || cities ==null || licensedDealerNumber < 0 || supplyServiceName == null ) {
                throw new Exception("The system has not been able to add the supply service due to invalid details");
            }
        } catch (Exception e) {
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

    }

    public void removeExternalSupplyService(int licensedDealerNumber, String systemManagerId) throws Exception {

            try {
                synchronized (managersLock) {
                    if (!systemManagerIds.contains(systemManagerId)) {
                        throw new Exception("Only system manager is allowed to remove external supply service");
                    }
                }
                if (supplyServicesFacade.getAllSupplyServices().size() <= 1) {
                    throw new Exception("There must remain at least one external supply service in the system");
                }
                supplyServicesFacade.removeExternalService(licensedDealerNumber);
            } catch (Exception e) {
                throw e;  // Re-throwing the exception to be handled by the caller
            }
    }

    public Set<String> getSystemManagerIds(){
        synchronized (managersLock) {
            return systemManagerIds;
        }
    }

    public void purchase(String userID, PaymentDTO paymentDTO, UserDTO userDTO) throws Exception {
        CartDTO cartDTO = null;
        try{
            cartDTO = checkingCartValidationBeforePurchase(userID, userDTO);
            payWithExternalPaymentService(cartDTO, paymentDTO, userID);
        }
        catch (Exception e){
            if (cartDTO != null) {
                returnCartToStock(cartDTO.getStoreToProducts());
            }
            throw new Exception (e);
        }
    }


    public void payWithExternalPaymentService(CartDTO cartDTO,PaymentDTO payment, String userId) throws Exception{
        if(cartDTO.getCartPrice()<= 0 || payment.getMonth()> 12 || payment.getMonth()<1 || payment.getYear() < 2020 ||payment.getHolderId()==null ||cartDTO.getStoreToProducts()==null) {
            throw new Exception("There is a problem with the provided payment measure or details of the order.\n");
        }
        if(paymentServicesFacade.getAllPaymentServices().size()<1){
            throw new Exception("There is no available external payment system.\n");
        }
        Map<String,String> receiptIdStoreId = paymentServicesFacade.pay(cartDTO.getCartPrice(), payment, userId, cartDTO.getStoreToProducts()); //<receiptId, storeId>
        //print when implement notifications (purchase successes)

        //Add the receiptId and storeId to the user receipts map
        if (userFacade.isMember(userId))
        {
            userFacade.addReceiptToUser(receiptIdStoreId, userId);
        }
        //Add the receiptId and userId to the store receipts map
        for (String receiptId : receiptIdStoreId.keySet()) {
            storeFacade.addReceiptToStore(receiptIdStoreId.get(receiptId), receiptId, userId);
        }
    }

    public void paymentFailed(CartDTO cartDTO) throws Exception {
        returnCartToStock(cartDTO.getStoreToProducts());
    }

    //Map<StoreID, Map<ProductName, quantity>>
    public void returnCartToStock(Map<String, Map<String, Integer>> products){
        for (String storeId: products.keySet()){
            storeFacade.returnProductToStore(products.get(storeId), storeId);
        }
    }

    public void logout(String userId){
        //todo add condition if the user is logged in
        userFacade.getUserByID(userId).Logout();
        authenticationAndSecurityFacade.removeToken(userId);
    }


    public void exitMarketSystem(String userId){
        userFacade.exitMarketSystem(userId);
    }


    public String enterMarketSystem(){
        return userFacade.addUser();
    }

    public void register( String userId, UserDTO user, String password) throws Exception {
        //check password validation
        if (password == null || password.equals("")){
            throw new Exception("All fields are required.");
        }
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        userFacade.register(userId, user, encryptedPassword);
        authenticationAndSecurityFacade.generateToken(userId);
    }

    public void Login(String userId,String username, String password) throws Exception {
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        userFacade.Login(userId, username,encryptedPassword);
        authenticationAndSecurityFacade.generateToken(userId);
    }

    public void addProductToBasket(String productName, int quantity, String storeId, String userId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        storeFacade.checkQuantityAndPolicies(productName, quantity, storeId, userId);
        int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

    }

    public void removeProductFromBasket(String productName, String storeId, String userId)throws Exception
    {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.checkIfCanRemove(productName, storeId, userId);
        userFacade.removeItemFromUserCart(productName, storeId, userId);
    }


    public void openStore(String user_ID, String name, String description)throws Exception {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
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
        String store_ID = this.storeFacade.openStore(name, description);
        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        this.roleFacade.createStoreOwner(member_ID, store_ID, true, "no nominator");
    }

    public void addProductToStore(String userId, String storeId, ProductDTO product) throws Exception {
        if (userFacade.getUserByID(userId) != null){
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null){
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.addProductToStore(storeId, product);
                    } else {
                        throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void removeProductFromStore(String userId, String storeId, String productName) throws Exception {
        if (userFacade.getUserByID(userId) != null){
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.removeProductFromStore(storeId, productName);
                    } else {
                        throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void updateProductInStore(String userId, String storeId, ProductDTO product) throws Exception {
        if (userFacade.getUserByID(userId) != null) {
            if (userFacade.isMember(userId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(userId));
                if (!succeeded) {
                    logout(userId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String memberId = userFacade.getMemberIdByUserId(userId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                            (roleFacade.verifyStoreManager(storeId, memberId) &&
                                    roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
                        storeFacade.updateProductInStore(storeId, product);
                    } else {
                        throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.createStoreOwner(nominatedMemberID, storeId, false, nominatorMemberID);
                        } else {
                            throw new Exception(ExceptionsEnum.usernameNotFound.toString());
                        }
                    } else {
                        throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void appointStoreManager(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.createStoreManager(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
                        } else {
                            throw new Exception(ExceptionsEnum.usernameNotFound.toString());
                        }
                    } else {
                        throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        if (userFacade.getUserByID(nominatorUserId) != null) {
            if (userFacade.isMember(nominatorUserId)) {
                boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorUserId));
                if (!succeeded) {
                    logout(nominatorUserId);
                    throw new Exception(ExceptionsEnum.sessionOver.toString());
                }
                String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
                if (storeFacade.getStoreByID(storeId) != null) {
                    if (roleFacade.verifyStoreOwner(storeId, nominatorMemberID)) {
                        if (userFacade.getMemberByUsername(nominatedUsername) != null) {
                            String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
                            roleFacade.updateStoreManagerPermissions(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
                        } else {
                            throw new Exception(ExceptionsEnum.usernameNotFound.toString());
                        }
                    } else {
                        throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
                    }
                } else {
                    throw new Exception(ExceptionsEnum.storeNotExist.toString());
                }
            } else {
                throw new Exception(ExceptionsEnum.userIsNotStoreOwner.toString());
            }
        } else {
            throw new Exception(ExceptionsEnum.userNotExist.toString());
        }
    }

    public void closeStore(String user_ID, String store_ID) throws Exception
    {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.isUserLoggedInError(user_ID);
        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
        roleFacade.verifyStoreOwnerIsFounder(store_ID, member_ID);
        storeFacade.verifyStoreExistError(store_ID);
        storeFacade.closeStore(store_ID);
        List<String> storeManagers = roleFacade.getAllStoreManagers(store_ID);
        List<String> storeOwners = roleFacade.getAllStoreOwners(store_ID);
        //todo: add function which send notification to all store roles (notification component).
        //todo: update use-case parameters

    }

    public Map<String, String> getInformationAboutRolesInStore(String user_ID, String store_ID) throws Exception {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<String, String> information = null;

        userFacade.isUserLoggedInError(user_ID);
        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
        storeFacade.verifyStoreExistError(store_ID);
        information = roleFacade.getInformationAboutStoreRoles(store_ID);

        return information;
    }


    public Map<String, List<Integer>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID) throws Exception {

        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<String, List<Integer>> managersAuthorizations;

        userFacade.isUserLoggedInError(user_ID);

        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
        storeFacade.verifyStoreExistError(store_ID);
        managersAuthorizations = roleFacade.getStoreManagersAuthorizations(store_ID);


        return managersAuthorizations;
    }


   // public List<Integer> getInformationAboutStores(String user_ID)throws Exception

    public Map<String, Map<String, Integer>> getPurchaseList(String userId)throws Exception{
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<String, Map<String, Integer>> purchaseList = new HashMap<>();
        List<String> usersStores = userFacade.getCartStoresByUser(userId);
        for (String storeId: usersStores) {
            Map<String, Integer> productAndQuantity = new HashMap<>();
            purchaseList.put(storeId, productAndQuantity) ;
            Map <String, List<Integer>> returnedMap = userFacade.getCartProductsByStoreAndUser(storeId , userId);
            for (String productName : returnedMap.keySet()){
                productAndQuantity.put(productName,returnedMap.get(productName).get(0) );
            }

        }
        return purchaseList;
    }

    public List<String> getInformationAboutStores(String user_ID) throws Exception {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<String> openedStores = storeFacade.getInformationAboutOpenStores(); // open stores available for everyone
        List<String> closedStores = storeFacade.getInformationAboutClosedStores(); //closed stores available only for owners/ system managers
        List<String> closedStoreAvailable = null;

        userFacade.isUserLoggedInError(user_ID);
        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        if (!this.roleFacade.verifyMemberIsSystemManager(user_ID))
            closedStoreAvailable = roleFacade.getStoresByOwner(closedStores, member_ID);
        else
            closedStoreAvailable = closedStores;

        List<String> allAvailableStores = new ArrayList<>(openedStores);
        if (closedStoreAvailable != null) {
            allAvailableStores.addAll(closedStoreAvailable);
        }

        return allAvailableStores;
    }

    public void modifyShoppingCart(String productName, int quantity, String storeId, String userId)throws Exception
    {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
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
            storeFacade.checkQuantityAndPolicies(productName, quantity, storeId, userId);
            int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
            userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
        }
    }

    public Map<String, Integer> marketManagerAskInfo(String user_ID)throws Exception
    {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        userFacade.isUserLoggedInError(user_ID);
        roleFacade.verifyMemberIsSystemManagerError(user_ID);
        return paymentServicesFacade.getStorePurchaseInfo();
    }



    public Map<String, Integer> storeOwnerGetInfoAboutStore(String user_ID, String store_ID) throws Exception //return receiptId and total amount in the receipt for the specific store
    {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<String, Integer> storeReceiptsAndTotalAmount;
        userFacade.isUserLoggedInError(user_ID);
        String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
        storeFacade.verifyStoreExistError(store_ID);
        storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount(store_ID);

        if (storeReceiptsAndTotalAmount.isEmpty())
            throw new IllegalArgumentException("There are no purchases in the store");
        return storeReceiptsAndTotalAmount;
    }

    public CartDTO checkingCartValidationBeforePurchase(String user_ID,UserDTO userDTO) throws Exception {
        if (userFacade.isMember(user_ID)) {
            String memberId = userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        int totalPrice = 0;
        this.userFacade.isUserCartEmpty(user_ID);

        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);
        for(String store_ID: stores)
        {
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
            int quantity;
            for(String productName: products.keySet()) {
                quantity = products.get(productName).get(0);
                this.storeFacade.checkQuantityAndPolicies(productName, quantity, store_ID, user_ID);
                int availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
                this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), availableExternalSupplyService, userDTO.getAddress(), user_ID);
            }

            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            int storeTotalPrice = this.storeFacade.calculateTotalCartPriceAfterDiscount(store_ID, products, storeTotalPriceBeforeDiscount);
            totalPrice += storeTotalPrice;
        }
        //remove items from stock
        removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID,totalPrice,getPurchaseList(user_ID));
    }

    public int checkAvailableExternalSupplyService(String country, String city) throws Exception {
        int availibleExteranlSupplyService =this.supplyServicesFacade.checkAvailableExternalSupplyService(country,city);
        if(-1==availibleExteranlSupplyService)
            throw new Exception(ExceptionsEnum.ExternalSupplyServiceIsNotAvailable.toString());
        return availibleExteranlSupplyService;
    }

    public void createShiftingDetails(String country, String city, int availibleExteranlSupplyService, String address, String user_ID) throws Exception
    {
        String userName = this.userFacade.getUserByID(user_ID).getName();
        if(!supplyServicesFacade.createShiftingDetails(availibleExteranlSupplyService, userName,country,city,address)){
            throw new Exception(ExceptionsEnum.createShiftingError.toString());
        }
    }

    public void removeUserCartFromStock(String userId) throws Exception {
        if (userFacade.getUserByID(userId) == null) {
            throw new Exception("User not found.");
        }
        if (userFacade.getUserByID(userId).getCart() == null || userFacade.getUserByID(userId).getCart().isCartEmpty()) {
            throw new Exception("User cart is empty, there's nothing to remove from stock.");
        }
        List<String> storeIds = userFacade.getUserByID(userId).getCart().getCartStores();
        for (String storeId : storeIds) {
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


    public List<String> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        storeFacade.checkCategory(categoryStr);
        storeFacade.verifyStoreExistError(storeId);
        return storeFacade.inStoreProductSearch(productName, categoryStr, keywords, storeId);
    }

    public List<String> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<String> stores = this.storeFacade.getStores();
        for(String store_ID: stores)
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

    public List<String> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating)throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        checkPrice(minPrice, maxPrice);
        checkProductRating(productMinRating);
        checkStoreRating(storeMinRating);
        storeFacade.checkCategory(categoryStr);
        storeFacade.verifyStoreExistError(storeId);
        List<String> filteredProductNames = storeFacade.inStoreProductFilter(categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);

        return filteredProductNames;
    }

    public List<String> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        List<String> filteredProductNames = new ArrayList<>();

        List<String > stores = this.storeFacade.getStores();
        for(String store_ID: stores)
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

    public void tokensChecking(String userId) throws Exception{
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }
    }

    public void checkPrice(Integer minPrice, Integer maxPrice)
    {
        if (!(minPrice == null || maxPrice == null) && !(minPrice != null && maxPrice != null && minPrice <= maxPrice))
            throw new IllegalArgumentException(ExceptionsEnum.priceRangeInvalid.toString());
    }

    public void checkProductRating(Double productMinRating)
    {

        if (productMinRating != null && (productMinRating < 0 || productMinRating > 5))
            throw new IllegalArgumentException(ExceptionsEnum.productRateInvalid.toString());
    }

    public void checkStoreRating(Double storeMinRating)
    {
        if (storeMinRating != null && (storeMinRating < 0 || storeMinRating > 5))
            throw new IllegalArgumentException(ExceptionsEnum.storeRateInvalid.toString());
    }
}
