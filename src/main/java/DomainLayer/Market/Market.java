package DomainLayer.Market;

import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;
import DomainLayer.Notifications.Notification;
import DomainLayer.Notifications.StoreNotification;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Role.RoleFacade;
import DomainLayer.Store.Product;
import Util.ExceptionsEnum;

import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.concurrent.*;



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


    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade){
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();

    }

    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade, SupplyServicesFacade supplyServicesFacade){
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        this.supplyServicesFacade= supplyServicesFacade;
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



    public String init(UserDTO user, String password, PaymentServiceDTO paymentServiceDTO,  SupplyServiceDTO supplyServiceDTO) throws Exception {
        synchronized (initializedLock) {
            if (initialized == true) {
                return null;
            }
        }
        try {
            // Check for supply service
            if (supplyServiceDTO.getSupplyServiceName() == null || supplyServiceDTO.getLicensedDealerNumber().length()<0 || supplyServiceDTO.getCountries()==null || supplyServiceDTO.getCities()==null ) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceDetails.toString());
            }
            // Check for payment service
            if (paymentServiceDTO.getPaymentServiceName() == null || paymentServiceDTO.getUrl()==null || paymentServiceDTO.getLicensedDealerNumber() ==null) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceDetails.toString());
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
        user.setUserId(firstUserID);
        String systemMangerId = userFacade.register(firstUserID, user, encrypted);
        synchronized (managersLock) {
            systemManagerIds.add(systemMangerId);

        }
        paymentServicesFacade.addExternalService(paymentServiceDTO);
        supplyServicesFacade.addExternalService(supplyServiceDTO);
        synchronized (initializedLock) {
            initialized = true;
        }
        return firstUserID;
    }

    public void addExternalPaymentService(PaymentServiceDTO paymentServiceDTO, String systemMangerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemMangerId)) {
                    throw new Exception(ExceptionsEnum.SystemManagerPaymentAuthorization.toString());
                }
            }
            if (paymentServiceDTO.getPaymentServiceName() == null || paymentServiceDTO.getLicensedDealerNumber()==null || paymentServiceDTO.getUrl()==null ) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceParameters.toString());
            }
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        paymentServicesFacade.addExternalService(paymentServiceDTO);
    }

    public void removeExternalPaymentService(String licensedDealerNumber, String systemMangerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemMangerId)) {
                    throw new Exception(ExceptionsEnum.SystemManagerPaymentAuthorizationRemove.toString());
                }
            }
            if (paymentServicesFacade.getAllPaymentServices().size() <= 1) {
                throw new Exception(ExceptionsEnum.OnlyPaymentService.toString());
            }
        }
        catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        paymentServicesFacade.removeExternalService(licensedDealerNumber);

    }

    public void addExternalSupplyService(SupplyServiceDTO supplyServiceDTO, String systemManagerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemManagerId)) {
                    throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorization.toString());
                }
            }
            if (supplyServiceDTO.getSupplyServiceName() == null || supplyServiceDTO.getCountries() ==null || supplyServiceDTO.getCities() ==null || Integer.parseInt(supplyServiceDTO.getLicensedDealerNumber())< 0  ) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceParameters.toString());
            }
        } catch (Exception e) {
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        supplyServicesFacade.addExternalService(supplyServiceDTO);

    }

    public void removeExternalSupplyService(String licensedDealerNumber, String systemManagerId) throws Exception {

            try {
                synchronized (managersLock) {
                    if (!systemManagerIds.contains(systemManagerId)) {
                        throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorizationRemove.toString());
                    }
                }
                if (supplyServicesFacade.getAllSupplyServices().size() <= 1) {
                    throw new Exception(ExceptionsEnum.OnlySupplyService.toString());
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

    public void purchase(PaymentDTO paymentDTO, UserDTO userDTO) throws Exception {
        CartDTO cartDTO = null;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> timeoutHandle = null;

        try {
            cartDTO = checkingCartValidationBeforePurchase(userDTO.getUserId(), userDTO);

            // Create a CompletableFuture for user input
//            CompletableFuture<Void> userInputFuture = new CompletableFuture<>();
//
//            // Schedule a task to complete the future with an exception if the user does not respond in time
//            timeoutHandle = scheduler.schedule(() -> {
//                userInputFuture.completeExceptionally(new TimeoutException("User input time expired"));
//            }, 5, TimeUnit.SECONDS);
//
//            // Here you should integrate your actual user input mechanism
//            // For this example, we simulate user input with a manual completion
//            // In a real application, replace this line with actual user input handling
//            // userInputFuture.complete(null);  // Uncomment this to simulate user input completion
//
//            // Wait for user input or timeout
//            userInputFuture.get();

            // Proceed with payment if user input is received
            payWithExternalPaymentService(cartDTO, paymentDTO, userDTO.getUserId());
        } catch (Exception e) {
            if (cartDTO != null) {
                returnCartToStock(cartDTO.getStoreToProducts());
            }
            throw e;
        } finally {
            if (timeoutHandle != null && !timeoutHandle.isDone()) {
                timeoutHandle.cancel(true);
            }
            scheduler.shutdown();
        }
    }

    public void purchaseForTest(PaymentDTO paymentDTO, UserDTO userDTO) throws Exception {
        CartDTO cartDTO = null;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> timeoutHandle = null;

        try {
            cartDTO = checkingCartValidationBeforePurchase1(userDTO.getUserId(), userDTO);

            // Create a CompletableFuture for user input
//            CompletableFuture<Void> userInputFuture = new CompletableFuture<>();
//
//            // Schedule a task to complete the future with an exception if the user does not respond in time
//            timeoutHandle = scheduler.schedule(() -> {
//                userInputFuture.completeExceptionally(new TimeoutException("User input time expired"));
//            }, 5, TimeUnit.SECONDS);
//
//            // Here you should integrate your actual user input mechanism
//            // For this example, we simulate user input with a manual completion
//            // In a real application, replace this line with actual user input handling
//            // userInputFuture.complete(null);  // Uncomment this to simulate user input completion
//
//            // Wait for user input or timeout
//            userInputFuture.get();

            // Proceed with payment if user input is received
            //payWithExternalPaymentService(cartDTO, paymentDTO, userDTO.getUserId());
        } catch (Exception e) {
            if (cartDTO != null) {
                returnCartToStock(cartDTO.getStoreToProducts());
            }
            throw e;
        } finally {
            if (timeoutHandle != null && !timeoutHandle.isDone()) {
                timeoutHandle.cancel(true);
            }
            scheduler.shutdown();
        }
    }

    public CartDTO checkingCartValidationBeforePurchase1(String user_ID,UserDTO userDTO) throws Exception {
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
            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
            for(String productName: products.keySet()) {
                quantity = products.get(productName).get(0);
                this.storeFacade.checkQuantity(productName, quantity, store_ID);
            }

//            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
//            int priceToReduce = storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);

            String availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
           // this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), availableExternalSupplyService, userDTO.getAddress(), user_ID);

            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            int storeTotalPrice = storeTotalPriceBeforeDiscount - 100;

//            int storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
            totalPrice += storeTotalPrice;
        }
        //remove items from stock
        //removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID,totalPrice,getPurchaseList(user_ID));
    }



    public void payWithExternalPaymentService(CartDTO cartDTO,PaymentDTO payment, String userId) throws Exception{
        if(cartDTO.getCartPrice()<= 0 || payment.getMonth()> 12 || payment.getMonth()<1 || payment.getYear() < 2020 ||payment.getHolderId()==null ||cartDTO.getStoreToProducts()==null) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidCreditCardParameters.toString());
        }
        if(paymentServicesFacade.getAllPaymentServices().size()<1){
            throw new Exception(ExceptionsEnum.noAvailableExternalPaymentService.toString());
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
    public void returnCartToStock(Map<String, Map<String, List<Integer>>> products){
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

    public String register( String userId, UserDTO user, String password) throws Exception {
        //check password validation
        if (password == null || password.equals("")){
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }
        if (!checkPasswordValidation(password)){
            throw new Exception("password must contains at least one digit, lowercase letter and uppercase letter.\n password must contains at least 8 characters");
        }
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        String memberId = userFacade.register(userId, user, encryptedPassword);
        //authenticationAndSecurityFacade.generateToken(memberId);
        return memberId;
    }



    public boolean checkPasswordValidation(String password){
        String PASSWORD_PATTERN =
                "^(?=.*[0-9])" +           // at least one digit
                "(?=.*[a-z])" +            // at least one lowercase letter
                "(?=.*[A-Z])" +            // at least one uppercase letter
                ".{8,}$";
        return Pattern.compile(PASSWORD_PATTERN).matcher(password).matches();

    }

    public String Login(String userId,String username, String password) throws Exception {
        String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
        String memberId = userFacade.Login(userId, username,encryptedPassword);
        authenticationAndSecurityFacade.generateToken(memberId);
        return memberId;
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

        storeFacade.checkQuantity(productName, quantity, storeId);
        Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(storeId, userId);
        products.put(productName, new ArrayList<>(Arrays.asList(quantity)));
        List<ProductDTO> productDTOS = storeFacade.getProductsDTOSByProductsNames(products, storeId);
        UserDTO user = new UserDTO(userFacade.getUserByID(userId));
        storeFacade.checkPurchasePolicy(user, productDTOS, storeId);
        int priceToReduce = storeFacade.calcDiscountPolicy(user, productDTOS, storeId);
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


    public String openStore(String user_ID, String name, String description)throws Exception {
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
        return store_ID;
    }

    public void addProductToStore(String userId, String storeId, ProductDTO product) throws Exception {
        userFacade.errorIfUserNotExist(userId);
        userFacade.errorIfUserNotMember(userId);
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.addProductToStore(storeId, product);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    public void removeProductFromStore(String userId, String storeId, String productName) throws Exception {
        userFacade.errorIfUserNotExist(userId);
        userFacade.errorIfUserNotMember(userId);
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }

        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.removeProductFromStore(storeId, productName);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    public void updateProductInStore(String userId, String storeId, ProductDTO product) throws Exception {
        userFacade.errorIfUserNotExist(userId);
        userFacade.errorIfUserNotMember(userId);
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }

        storeFacade.errorIfStoreNotExist(storeId);
        if (roleFacade.verifyStoreOwner(storeId, memberId) ||
                (roleFacade.verifyStoreManager(storeId, memberId) &&
                        roleFacade.managerHasInventoryPermissions(memberId, storeId))) {
            storeFacade.updateProductInStore(storeId, product);
        } else {
            throw new Exception(ExceptionsEnum.noInventoryPermissions.toString());
        }
    }

    public void appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        userFacade.errorIfUserNotMember(nominatorUserId);
        String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorMemberID));
        if (!succeeded) {
            logout(nominatorUserId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.createStoreOwner(nominatedMemberID, storeId, false, nominatorMemberID);
    }

    public void appointStoreManager(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
        userFacade.errorIfUserNotMember(nominatorUserId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorMemberID));
        if (!succeeded) {
            logout(nominatorUserId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.createStoreManager(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
    }

    public void updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeId,
                                    boolean inventoryPermissions, boolean purchasePermissions) throws Exception {
        userFacade.errorIfUserNotExist(nominatorUserId);
        userFacade.errorIfUserNotMember(nominatorUserId);
        String nominatorMemberID = userFacade.getMemberIdByUserId(nominatorUserId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(nominatorMemberID));
        if (!succeeded) {
            logout(nominatorUserId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }

        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.verifyStoreOwnerError(storeId, nominatorMemberID);
        userFacade.errorIfUsernameNotFound(nominatedUsername);
        String nominatedMemberID = userFacade.getMemberByUsername(nominatedUsername).getMemberID();
        roleFacade.updateStoreManagerPermissions(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
    }

    public List<ProductDTO> getStoreProducts(String storeId){
        return storeFacade.getStoreProductsDTO(storeId);
    }

    public List<UserDTO> getStoreWorkers(String storeId){
        List<UserDTO> workers = new ArrayList<>();
        List<String> managersIdList = roleFacade.getAllStoreManagers(storeId);
        workers.addAll(userFacade.getUserDTOByMemberId(managersIdList));
        List<String> ownersIdList2 = roleFacade.getAllStoreOwners(storeId);
        workers.addAll(userFacade.getUserDTOByMemberId(ownersIdList2));
        return workers;

    }

    public List<UserDTO> getStoreManagersDTO(String storeId){
        List<UserDTO> managers = new ArrayList<>();
        List<String> managersIdList = roleFacade.getAllStoreManagers(storeId);
        managers.addAll(userFacade.getUserDTOByMemberId(managersIdList));

        return managers;

    }

    public List<UserDTO> getStoreOwnersDTO(String storeId){
        List<UserDTO> owners = new ArrayList<>();
        List<String> ownersIdList = roleFacade.getAllStoreOwners(storeId);
        owners.addAll(userFacade.getUserDTOByMemberId(ownersIdList));
        return owners;

    }

    public List<String> getStoreManagers(String storeId){

        return roleFacade.getAllStoreManagers(storeId);


    }
    public List<String> getStoreOwners(String storeId){

        return roleFacade.getAllStoreOwners(storeId);

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
        String storeName = storeFacade.getStoreByID(store_ID).getStoreName();

        Notification n =new StoreNotification(storeName,"The store is now inactive");
        sendMessageToStaffOfStore(n,member_ID);
        //todo: update use-case parameters

    }
    public void sendMessageToStaffOfStore(Notification notification, String member_ID) {
        userFacade.getUserByID(member_ID).notifyObserver(notification);
//       // founder.notifyObserver(notification);
//        for (User u : getOwnersOfStore())
//            u.notifyObserver(notification);
//        for (User u : getManagersOfStore())
//            u.notifyObserver(notification);
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
        storeFacade.verifyStoreExistError(store_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
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
        storeFacade.verifyStoreExistError(store_ID);
        roleFacade.verifyStoreOwnerError(store_ID, member_ID);
        managersAuthorizations = roleFacade.getStoreManagersAuthorizations(store_ID);


        return managersAuthorizations;
    }

    public Map<String, Map<String, List<Integer>>> getPurchaseList(String userId)throws Exception{
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }
        Map<String, Map<String, List<Integer>>> purchaseList = new HashMap<>();
        List<String> usersStores = userFacade.getCartStoresByUser(userId);
        for (String storeId: usersStores) {
            Map<String, List<Integer>> productAndQuantity = new HashMap<>();
            purchaseList.put(storeId, productAndQuantity) ;
            Map <String, List<Integer>> returnedMap = userFacade.getCartProductsByStoreAndUser(storeId , userId);
            for (String productName : returnedMap.keySet()){
                productAndQuantity.put(productName,returnedMap.get(productName));
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

        if(userFacade.isMember(user_ID)) {
            String member_ID = this.userFacade.getMemberIdByUserId(user_ID);
            if (!this.roleFacade.verifyMemberIsSystemManager(user_ID))
                closedStoreAvailable = roleFacade.getStoresByOwner(closedStores, member_ID);
            else
                closedStoreAvailable = closedStores; //all stores are available for system managers
        }

        List<String> allAvailableStores = new ArrayList<>(openedStores);
        if (closedStoreAvailable != null) {
            allAvailableStores.addAll(closedStoreAvailable);
        }

        return allAvailableStores;
    }

    public List<String> getStoreCategories(){
        return storeFacade.getStoreCategories();
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
            userFacade.checkIfCanRemove(productName, storeId, userId);
            storeFacade.checkQuantity(productName, quantity, storeId);
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(storeId, userId);
            int totalPrice = storeFacade.calcPrice(productName, quantity, storeId, userId);
            products.put(productName, new ArrayList<>(Arrays.asList(quantity, totalPrice)));
            List<ProductDTO> productDTOS = storeFacade.getProductsDTOSByProductsNames(products, storeId);
            UserDTO user = new UserDTO(userFacade.getUserByID(userId));
            storeFacade.checkPurchasePolicy(user, productDTOS, storeId);
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
        if (!systemManagerIds.contains(userFacade.getMemberIdByUserId(user_ID)))
            throw new IllegalArgumentException(ExceptionsEnum.notSystemManager.toString());
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
            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
            for(String productName: products.keySet()) {
                quantity = products.get(productName).get(0);
                this.storeFacade.checkQuantity(productName, quantity, store_ID);
            }

            storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
            int priceToReduce = storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);

            String availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
            this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), availableExternalSupplyService, userDTO.getAddress(), user_ID);

            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            int storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
            totalPrice += storeTotalPrice;
        }
        //remove items from stock
        removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID,totalPrice,getPurchaseList(user_ID));
    }

    public String checkAvailableExternalSupplyService(String country, String city) throws Exception {
        String availibleExteranlSupplyService =this.supplyServicesFacade.checkAvailableExternalSupplyService(country,city);
        if("-1".equals(availibleExteranlSupplyService)) {
            throw new Exception(ExceptionsEnum.NoExternalSupplyService.toString());
        }
        if("-2".equals(availibleExteranlSupplyService)) {
            throw new Exception(ExceptionsEnum.ExternalSupplyServiceIsNotAvailableForArea.toString());
        }
        return availibleExteranlSupplyService;
    }

    public void createShiftingDetails(String country, String city, String availibleExteranlSupplyService, String address, String user_ID) throws Exception
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
            Map<String, Integer> products = userFacade.getUserByID(userId).getCart().getProductsQuantityByStore(storeId); //Map<productName, quantity> products
            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                storeFacade.getStoreByID(storeId).removeProductQuantity(productName,quantity);
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

    public List<ProductDTO> inStoreProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords, String storeId) throws Exception {
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
        return storeFacade.inStoreProductSearchDTO(productName, categoryStr, keywords, storeId);
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

        storeFacade.checkCategory(categoryStr);

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
        return filteredProductNames;
    }

    public Map<String, List<ProductDTO>> generalProductSearchDTO(String userId, String productName, String categoryStr, List<String> keywords) throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }

        storeFacade.checkCategory(categoryStr);

        Map<String, List<ProductDTO>> filteredProductNames = new HashMap<>();
        List<String> stores = this.storeFacade.getStores();
        for(String store_ID: stores)
        {
            try {

                filteredProductNames.put(store_ID, inStoreProductSearchDTO(userId, productName, categoryStr, keywords,store_ID));
            }
            catch (Exception e)
            {
                continue;
            }
        }
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
        checkPrice(minPrice, maxPrice);
        checkProductRating(productMinRating);
        checkStoreRating(storeMinRating);
        storeFacade.checkCategory(categoryStr);

        List<String> filteredProductNames = new ArrayList<>();

        List<String > stores = this.storeFacade.getStores();
        for(String store_ID: stores)
        {
            try{
                filteredProductNames.addAll(inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, store_ID, productsFromSearch, storeMinRating));
            }catch (Exception e)
            {
                continue;
            }
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

    public void addPurchaseRuleToStore(List<Integer> ruleNums, List<String> operators, String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        checkLogicalRulesAndOperators(ruleNums, operators);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addPurchaseRuleToStore(ruleNums, operators, storeId);
    }

    public void removePurchaseRuleFromStore(int ruleNum,  String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.removePurchaseRuleFromStore(ruleNum, storeId);
    }

    public void addDiscountCondRuleToStore(List<Integer> ruleNums, List<String> logicOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId ,String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        checkLogicalRulesAndOperators(ruleNums, logicOperators);
        checkNumericalRulesAndOperators(discDetails, numericalOperators);
        checkProductDiscountDetails(discDetails);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addDiscountCondRuleToStore(ruleNums, logicOperators, discDetails, numericalOperators, storeId);
    }


    public void addDiscountSimpleRuleToStore(List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId ,String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        checkNumericalRulesAndOperators(discDetails, numericalOperators);
        checkProductDiscountDetails(discDetails);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addDiscountSimpleRuleToStore(discDetails, numericalOperators, storeId);
    }

    public void removeDiscountRuleFromStore(int ruleNum, String userId ,String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.removeDiscountRuleFromStore(ruleNum, storeId);
    }

    private void checkProductDiscountDetails(List<DiscountValueDTO> discDetails) {
        for (DiscountValueDTO discountValueDTO : discDetails) {
            int count = 0;
            if (discountValueDTO.getCategory() != null) {
                count++;
            }
            if (discountValueDTO.getIsStoreDiscount()) {
                count++;
            }
            if (discountValueDTO.getProductsNames() != null) {
                count++;
            }
            if (count != 1) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidDiscountValueParameters.toString());
            }
        }
    }

    public void checkLogicalRulesAndOperators(List<Integer> ruleNums, List<String> operators) throws Exception {
        if (ruleNums.size() != operators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < operators.size(); i++) {
            if (!operators.get(i).equals("AND") && !operators.get(i).equals("OR") && !operators.get(i).equals("COND") && !operators.get(i).equals("XOR")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }

    public void checkNumericalRulesAndOperators(List<DiscountValueDTO> discDetails, List<String> numericalOperators) throws Exception {
        if (discDetails.size() != numericalOperators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < numericalOperators.size(); i++) {
            if (!numericalOperators.get(i).equals("MAX") && !numericalOperators.get(i).equals("ADD")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }
}
