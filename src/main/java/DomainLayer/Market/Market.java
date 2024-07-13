package DomainLayer.Market;

import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;

//import DomainLayer.Notifications.Notification;
import DomainLayer.Notifications.LateNotificationFacade;
//import DomainLayer.Notifications.StoreNotification;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Repositories.InitializedDBRepository;
import DomainLayer.Repositories.InitializedRepository;
import DomainLayer.Role.RoleFacade;

import DomainLayer.Store.Product;

//import PresentationLayer.Vaadin.NotificationsEndPoint;
//import PresentationLayer.WAF.NotificationService;
import PresentationLayer.Vaadin.MyWebSocketHandler;
import PresentationLayer.WAF.Service_layer;
import Util.ExceptionsEnum;

import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.*;

import jakarta.inject.Qualifier;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.Month;
import java.time.Year;
import java.util.Map;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.concurrent.*;


@Service
public class Market {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private static Market MarketInstance;
    private PaymentServicesFacade paymentServicesFacade;
    private SupplyServicesFacade supplyServicesFacade;
    private Set<String> systemManagerIds;
    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;
    private StoreFacade storeFacade;
    private UserFacade userFacade;
    private RoleFacade roleFacade;
    //private InitializedStatus initialized = new InitializedStatus(false);
    private InitializedRepository initializedRepository;
    private final Object initializedLock;
    private final Object managersLock;
    private final Object validationLock;


    private MyWebSocketHandler myWebSocketHandler;

    public synchronized static Market getInstance() {
//        if (MarketInstance == null) {
//            MarketInstance = new Market();
//        }
        return MarketInstance;
    }

    public Market() {
        StoreFacade storeFacade1 =  new StoreFacade();
        UserFacade userFacade1 =  new UserFacade();
        RoleFacade roleFacade1 =  new RoleFacade();
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade1 =  new AuthenticationAndSecurityFacade();
        PaymentServicesFacade paymentServicesFacade1 =  new PaymentServicesFacade();
        SupplyServicesFacade supplyServicesFacade1 = new SupplyServicesFacade();
        storeFacade=storeFacade1;
        userFacade = userFacade1;
        roleFacade = roleFacade1;
        authenticationAndSecurityFacade = authenticationAndSecurityFacade1;
        paymentServicesFacade = paymentServicesFacade1;
        supplyServicesFacade = supplyServicesFacade1;
        initializedLock= new Object();
        systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        //lateNotificationFacade = new LateNotificationFacade();

        MarketInstance = this;

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }



    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade, PaymentServicesFacade paymentServicesFacade,
                  SupplyServicesFacade supplyServicesFacade) {
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();


        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(PaymentServicesFacade paymentServicesFacade,
                  SupplyServicesFacade supplyServicesFacade,AuthenticationAndSecurityFacade authenticationAndSecurityFacade){
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = UserFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();


        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(UserFacade userFacade, AuthenticationAndSecurityFacade authenticationAndSecurityFacade,
                  StoreFacade storeFacade, SupplyServicesFacade supplyServicesFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = authenticationAndSecurityFacade;
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }
    public Market(UserFacade userFacade,  StoreFacade storeFacade ,
                  PaymentServicesFacade paymentServicesFacade, SupplyServicesFacade supplyServicesFacade){
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    public Market(UserFacade userFacade,  StoreFacade storeFacade ,
                  PaymentServicesFacade paymentServicesFacade, SupplyServicesFacade supplyServicesFacade, RoleFacade roleFacade) {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = roleFacade;
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade = supplyServicesFacade;
        initializedLock = new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler = MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade) {
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();

        //lateNotificationFacade = new LateNotificationFacade();

        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    @Autowired
    public Market(UserFacade userFacade, StoreFacade storeFacade, SupplyServicesFacade supplyServicesFacade,
                  PaymentServicesFacade paymentServicesFacade, RoleFacade roleFacade,
                  InitializedRepository initializedRepository) throws Exception {
        this.storeFacade = storeFacade;
        this.userFacade = userFacade;
        this.roleFacade = roleFacade;
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= supplyServicesFacade;
        this.initializedRepository = initializedRepository;
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        systemManagerIds = new HashSet<>();
        try{
            Optional<InitializedStatus> initialized = initializedRepository.findById("market");
            InitializedStatus initializedStatus = initialized.orElse(null);
            if (initializedStatus == null) {
                initializedRepository.save(new InitializedStatus(false));
            }
        }
        catch(Exception e){
            throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
        }
        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();
    }

    public Market(UserFacade userFacade, PaymentServicesFacade paymentServicesFacade){
        this.storeFacade = StoreFacade.getInstance();
        this.userFacade = userFacade;
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        initializedRepository = null;

        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    public Market(PaymentServicesFacade paymentServicesFacade, StoreFacade storeFacade){
        this.storeFacade = storeFacade;
        this.userFacade = UserFacade.getInstance();
        this.roleFacade = RoleFacade.getInstance();
        this.paymentServicesFacade = paymentServicesFacade;
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();
        this.supplyServicesFacade= SupplyServicesFacade.getInstance();
        initializedLock= new Object();
        this.systemManagerIds = new HashSet<>();
        managersLock = new Object();
        validationLock = new Object();
        initializedRepository = null;

        //lateNotificationFacade = new LateNotificationFacade();

        myWebSocketHandler =  MyWebSocketHandler.getInstance();

    }

    public PaymentServicesFacade getPaymentServicesFacade(){
        return this.paymentServicesFacade;
    }

    public SupplyServicesFacade getSupplyServicesFacade(){
        return this.supplyServicesFacade;
    }


    public synchronized Market newForTests(){
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
        //lateNotificationFacade = new LateNotificationFacade();


        //notificationService = new NotificationsEndPoint();
        myWebSocketHandler =  MyWebSocketHandler.getInstance();

        return MarketInstance;



    }

    public Map<String, Object> loadAdminConfiguration(String configFilePath) throws Exception {
        try (InputStream inputStream = new FileInputStream(configFilePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> config = yaml.load(inputStream);
            return config;
        }
    }

//    public boolean checkHandShake(){
//        return paymentServicesFacade.checkHandShake();
//    }


    @Transactional
    public String init() throws Exception {

        logger.info("Starting the initialization of the system.");
        Map<String, Object> adminConfig = null; // Declare adminConfig here
        String configFilePath = "src/main/resources/ConfigurationFile.yaml";
        Map<String, Object> initConfig = loadAdminConfiguration(configFilePath);
        Map<String, Object> paymentConfig  = (Map<String, Object>) initConfig.get("paymentService");
        String paymentURL = (String) paymentConfig.get("url");
        Map<String, Object> serviceConfig  = (Map<String, Object>) initConfig.get("supplyService");
        String supplyURL = (String) serviceConfig.get("url");

        String adminPassword = "";

        synchronized (initializedLock) {
            if (Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized()) {
                logger.info("system already initialized");
                return "null";
            }
        }
        try {
            // Check for supply service
            if (supplyURL == null) {
                logger.error("system cannot be initialized, supply url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceDetails.toString());
            }
            // Check for payment service
            if (paymentURL == null) {
                logger.error("system cannot be initialized, payment url problem");
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceDetails.toString());
            }


             adminConfig = (Map<String, Object>) initConfig.get("admin");
             adminPassword = (String) adminConfig.get("password");
            validateAdminPassword1(adminPassword);

        } catch (Exception e) {
            throw e; // Re-throw the exception to be handled by the caller
        }


            // Extract admin configuration values
            String adminUsername = (String) adminConfig.get("username");
            String adminBirthday = (String) adminConfig.get("birthday");
            String adminCountry = (String) adminConfig.get("country");
            String adminCity = (String) adminConfig.get("city");
            String adminAddress = (String) adminConfig.get("address");
            String adminName = (String) adminConfig.get("name");


            logger.info("try register system admin");
            // Encode admin password
            String encryptedPassword = authenticationAndSecurityFacade.encodePassword(adminPassword);

            // Register user and retrieve system manager ID
            String firstUserID = enterMarketSystem();
            UserDTO userDTO1 = new UserDTO(firstUserID, adminUsername, adminBirthday, adminCountry, adminCity, adminAddress, adminName);
            String systemManagerId = userFacade.register(firstUserID, userDTO1, encryptedPassword);
            roleFacade.addSystemManger(systemManagerId);
            synchronized (managersLock) {
                systemManagerIds.add(systemManagerId);
            }
            if (!paymentServicesFacade.addExternalService("pay by card" ,paymentURL)){
                throw new Exception("problem while adding external payment service");
            };
            if (!supplyServicesFacade.addExternalService(supplyURL)){
                throw new Exception("problem while adding external supply service");
            };
            synchronized (initializedLock) {
                try {
                    initializedRepository.save(new InitializedStatus(true));
                }
                catch (Exception e) {
                    throw new Exception(ExceptionsEnum.DatabaseIsNotConnected.toString());
                }
                logger.info("system initialized successfully");
            }

            return firstUserID; // Return the generated user ID
        }

    public void validateAdminPassword1(String adminPassword) throws Exception {
        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new Exception(ExceptionsEnum.emptyField.toString());
        }

        if (!checkPasswordValidation(adminPassword)) {
            throw new Exception("Password must contain at least one digit, one lowercase letter, one uppercase letter, and be at least 8 characters long.");
        }
    }

    private void registerUsersInitState(List<String> usersToRegister, Map<String, Object> usersData) throws Exception {
        for (Map.Entry<String, Object> entry : usersData.entrySet()) {
            String username = entry.getKey();
            if (usersToRegister.contains(username)) {
                Map<String, Object> userData = (Map<String, Object>) entry.getValue();
//
                logger.info("try register user");
                // Extract user details
                String username1 = (String) userData.get("username");
                String password = (String) userData.get("password");
                String birthday = (String) userData.get("birthday");
                String country = (String) userData.get("country");
                String city = (String) userData.get("city");
                String address = (String) userData.get("address");
                String name = (String) userData.get("name");

                String encryptedPassword = authenticationAndSecurityFacade.encodePassword(password);
                // Register user and retrieve system manager ID
                String userId = enterMarketSystem();
                UserDTO userDTO1 = new UserDTO(userId, username1, birthday, country, city, address, name);
                String memberId = userFacade.register(userId, userDTO1, encryptedPassword);
                logger.info(" user registered successfully");
            }
        }
    }

    public int cancelPayment(String userId,  String transactionID) throws Exception {
        if (userFacade.isMember(userId)) {
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));if (!succeeded) {
                logout(userId);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }

        return userFacade.cancelPaynmet(userId , transactionID);
    }

    public int cancelSupply(String shippingId) throws Exception {
        return  supplyServicesFacade.cancelSupply(shippingId);

    }

    private Map<String, String> loginUsersInitState(List<String> userToLogin, Map<String, Object> users) throws Exception {
        Map<String, String> usernameToUserIdMap = new HashMap<>();
        for (String username : userToLogin) {
            logger.info("try login user");
            if(userFacade.getMembers().getByUserName(username) !=null){
                Map<String, Object> userData = (Map<String, Object>) users.get(username);
                String password = (String) userData.get("password");
                String userId = enterMarketSystem();
                Login(userId, username, password);
                usernameToUserIdMap.put(username, userId);
            }
            logger.info(" user logged in successfully");
        }
        return usernameToUserIdMap;
    }

    private void openStoresInitState( Map<String, Object> stores, Map<String, String> usernameToUserIdMap ) throws Exception {

        for (Map.Entry<String, Object> entry : stores.entrySet()) {
            logger.info(" try open new store");
            Map<String, Object> storeData = (Map<String, Object>) entry.getValue();
//
//            // Extract store details
            String usernameFounder = (String) storeData.get("userNameFounder");
            String founderId = usernameToUserIdMap.get(usernameFounder);
            String storeName = (String) storeData.get("storeName");
            String storeDescription = (String) storeData.get("description");
            openStore(founderId,storeName, storeDescription);
            logger.info(" store opened in successfully");
        }
    }

    private void appointManagerToStoreInitState( Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {
        String storeName=  (String) actionData.get("store");
        String storeId=  storeFacade.getStoreId(storeName);
        String userNameNominator=  (String) actionData.get("nominatorUser");
        List<String> nominatedUsers=  (List<String>) actionData.get("nominatedUsers");
        Boolean inventoryPermissions=  (Boolean) actionData.get("inventoryPermissions");
        Boolean purchasePermissions=  (Boolean) actionData.get("purchasePermissions");
        String nominatorId = usernameToUserIdMap.get(userNameNominator);
        for(String userName : nominatedUsers){
            logger.info(" try appoint manager to store");
            appointStoreManager(nominatorId, userName, storeId,inventoryPermissions ,purchasePermissions);
            logger.info(" manager appointed successfully");
        }

    }

    private void appointOwnerToStoreInitState( Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {
        String storeName=  (String) actionData.get("store");
        String storeId=  storeFacade.getStoreId(storeName);
        String userNameNominator=  (String) actionData.get("nominatorUser");
        List<String> nominatedUsers=  (List<String>) actionData.get("nominatedUsers");
        String nominatorId = usernameToUserIdMap.get(userNameNominator);
        for(String userName : nominatedUsers){
            logger.info(" try appoint owner to store");
            appointStoreOwner(nominatorId, userName, storeId);
            logger.info(" owner appointed successfully");
        }
    }



    private void addProductToStoreInitState(Map<String, Object> products, Map<String, Object> actionData, Map<String, String> usernameToUserIdMap) throws Exception {


        List<String> productToAdd = (List<String>) actionData.get("productName");
        String storeName=  (String) actionData.get("store");
        String userName=  (String) actionData.get("user");

        for (Map.Entry<String, Object> entry : products.entrySet()) {
            logger.info(" try add products to store");
            Map<String, Object> productData = (Map<String, Object>) entry.getValue();
            String productName=  (String) productData.get("productName");

//            // Extract store details
            if(productToAdd.contains(productName)){
                int productPrice=  (int) productData.get("price");
                int productQuantity=  (int) productData.get("quantity");
                String productDescription=  (String) productData.get("description");
                String productCategory=  (String) productData.get("categoryStr");
                ProductDTO productDTO = new ProductDTO(productName,productPrice, productQuantity,productDescription ,productCategory);

                String userId = usernameToUserIdMap.get(userName);
                String storeId=  storeFacade.getStoreId(storeName);
                addProductToStore(userId,  storeId, productDTO);

                logger.info(" add product to store successfully");
            }
        }
    }

    private void logOutInitState(List<String> usersToLogout, Map<String, String> usernameToUserIdMap) throws Exception {
        for (String username : usersToLogout) {
            logger.info(" try log out user");
            String userId = usernameToUserIdMap.get(username);
            logout(userId);
            logger.info(" user logout successfully");
        }
    }

        public void startStateInitialization() throws Exception {
        String configFilePath = "src/main/resources/stateAfterInit.yaml";
        Map<String, String> usernameToUserIdMap = new HashMap<>();
        Map<String, Object> stateConfig = loadAdminConfiguration(configFilePath);
        Map<String, Object> actions = (Map<String, Object>) stateConfig.get("actions");
        Map<String, Object> users = (Map<String, Object>) stateConfig.get("users");
        Map<String, Object> stores = (Map<String, Object>) stateConfig.get("stores");
        Map<String, Object> products = (Map<String, Object>) stateConfig.get("products");


            for (Map.Entry<String, Object> entry : actions.entrySet()) {
            String actionType = entry.getKey();
            Map<String, Object> actionData = (Map<String, Object>) entry.getValue();

            switch (actionType) {
                case "register_users":
                    List<String> userToRegister = (List<String>) actionData.get("users");
                    registerUsersInitState(userToRegister, users);
                    break;
                case "login_user":
                    List<String> userToLogin = (List<String>) actionData.get("users");
                    usernameToUserIdMap = loginUsersInitState(userToLogin, users);
                    break;
                case "open_store":
                    openStoresInitState(stores, usernameToUserIdMap);
                    break;
                case "add_product_to_store":
                    addProductToStoreInitState(products,actionData, usernameToUserIdMap);
                    break;
                case "set_manager_permission":
                    appointManagerToStoreInitState(actionData, usernameToUserIdMap);
                    break;
                case "set_Store_Owner":
                    appointOwnerToStoreInitState(actionData, usernameToUserIdMap);
                    break;
                case "LogOut":
                    List<String> logOutUsers=  (List<String>) actionData.get("users");
                    logOutInitState(logOutUsers, usernameToUserIdMap);
                    break;
            }
        }
    }


    public boolean checkInitializedMarket(){
        return Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized();
    }

    public void addExternalPaymentService(String paymentServiceName, String paymentURL, String systemMangerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemMangerId)) {
                    throw new Exception(ExceptionsEnum.SystemManagerPaymentAuthorization.toString());
                }
            }
            if (paymentURL== null  ) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidPaymentServiceParameters.toString());
            }
        } catch (Exception e) {
            // Log the error or handle it as needed
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        paymentServicesFacade.addExternalService(paymentServiceName, paymentURL);
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

    public void addExternalSupplyService(String SupplyURL, String systemManagerId) throws Exception {
        try {
            synchronized (managersLock) {
                if (!systemManagerIds.contains(systemManagerId)) {
                    throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorization.toString());
                }
            }
            if (SupplyURL == null   ) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidSupplyServiceParameters.toString());
            }
        } catch (Exception e) {
            throw e;  // Re-throwing the exception to be handled by the caller
        }
        supplyServicesFacade.addExternalService(SupplyURL);

    }

    public void removeExternalSupplyService(String supplyURL, String systemManagerId) throws Exception {

            try {
                synchronized (managersLock) {
                    if (!systemManagerIds.contains(systemManagerId)) {
                        throw new Exception(ExceptionsEnum.SystemManagerSupplyAuthorizationRemove.toString());
                    }
                }
                if (supplyServicesFacade.getAllSupplyServices().size() <= 1) {
                    throw new Exception(ExceptionsEnum.OnlySupplyService.toString());
                }
                supplyServicesFacade.removeExternalService(supplyURL);
            } catch (Exception e) {
                throw e;  // Re-throwing the exception to be handled by the caller
            }
    }

    public Set<String> getSystemManagerIds(){
        synchronized (managersLock) {
            return systemManagerIds;
        }
    }

    public String purchase(PaymentDTO paymentDTO,String paymentServiceName, UserDTO userDTO, CartDTO cartDTO) throws Exception {

        ScheduledFuture<?> timeoutHandle = null;
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        AtomicBoolean timeoutExpired = new AtomicBoolean(false);

        try {
            timeoutHandle = scheduler.schedule(() -> {
                timeoutExpired.set(true);
            }, 5L, TimeUnit.SECONDS);

            boolean userReadyToPay;
            for(userReadyToPay = false; !userReadyToPay && !timeoutExpired.get(); userReadyToPay = this.getUserConfirmationPurchase(userDTO.getUserId())) {
            }

            if (!userReadyToPay || timeoutExpired.get()) {
                throw new RuntimeException(ExceptionsEnum.TimeExpired.toString());
            }

            //Todo: we call payWithExternalPaymentService twice, check if needed
            //this.payWithExternalPaymentService(cartDTO, paymentDTO, userDTO.getUserId());
            String acquisitionId = this.payWithExternalPaymentService(cartDTO,paymentServiceName, paymentDTO, userDTO.getUserId());
            String availableExternalSupplyService = this.checkAvailableExternalSupplyService(userDTO.getCountry(), userDTO.getCity());
            this.createShiftingDetails(userDTO.getCountry(), userDTO.getCity(), availableExternalSupplyService, userDTO.getAddress(), userDTO.getUserId(),acquisitionId );
            sendMessagesOnPurchaseToStoreOwners(cartDTO);
            return acquisitionId;
        } catch (Exception exception) {
            Exception e = exception;
            if (cartDTO != null) {
                this.returnCartToStock(cartDTO.getStoreToProducts());
            }

            throw e;
        } finally {
            if (timeoutHandle != null && !timeoutHandle.isDone()) {
                timeoutHandle.cancel(true);
            }

            scheduler.shutdown();
        }

    }



    public void sendMessagesToOwnersAndManagers(String storeId , String message) throws Exception { // Inject VaadinUserService and NotificationService


        List<String> storeOwnerIds = roleFacade.getAllStoreOwners(storeId);
        //String storeName = storeFacade.getStoreName(storeId);

        List<String> storeManagersIds = roleFacade.getAllStoreManagers(storeId);
        Set<String> hasJob = new HashSet<>();
        hasJob.addAll(storeManagersIds);
        hasJob.addAll(storeOwnerIds);
        for (String memberId : hasJob) {
                //String message = "A purchase was made from your store - " + storeName;
            myWebSocketHandler.handleStringMessage(memberId, message); // Send real-time notification via WebSocket
        }
    }




    public void sendMessagesOnPurchaseToStoreOwners(CartDTO cartDTO) throws Exception { // Inject VaadinUserService and NotificationService

        for (String storeId : cartDTO.getStoreToProducts().keySet()) {
            List<String> storeOwnerIds = roleFacade.getAllStoreOwners(storeId);
            String storeName = storeFacade.getStoreName(storeId);

            for (String memberId : storeOwnerIds) {
                String message = "A purchase was made from your store - " + storeName;
                myWebSocketHandler.handleStringMessage(memberId, message); // Send real-time notification via WebSocket

            }
        }
    }

    public void setUserConfirmationPurchase(String userID) {
        this.userFacade.getUserByID(userID).setReadyToPay(true);
    }

    public boolean getUserConfirmationPurchase(String userID) {
        return this.userFacade.getUserByID(userID).isReadyToPay();
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

    public List<String> getUserNotifications(String memberId){
        return myWebSocketHandler.getUserNotifications(memberId);
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
                this.storeFacade.checkQuantityAndPrice(productName, quantity, store_ID);
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



    public String payWithExternalPaymentService(CartDTO cartDTO,String paymentServiceName ,PaymentDTO payment, String userId) throws Exception{
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH)+1;
        if(cartDTO.getCartPrice()<= 0 || payment.getMonth()> 12 || payment.getMonth()<1 || payment.getYear() < Year.now().getValue()
                ||payment.getHolderId()==null || payment.getCreditCardNumber().length()!=16 || cartDTO.getStoreToProducts()==null||
                (payment.getYear() == Year.now().getValue() && month>= payment.getMonth())) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidCreditCardParameters.toString());
        }
        if(paymentServicesFacade.getAllPaymentServices().size()<1){
            throw new Exception(ExceptionsEnum.noAvailableExternalPaymentService.toString());
        }
        String acquisitionId = paymentServicesFacade.pay(cartDTO.getCartPrice(), paymentServiceName, payment, userId, cartDTO.getStoreToProducts());
        Map<String,String> receiptIdStoreId = paymentServicesFacade.getAcquisitionReceipts(acquisitionId); //<receiptId, storeId>
        //print when implement notifications (purchase successes)

        //add acquisitionId to user
        userFacade.addAcquisitionToUser(userId,acquisitionId);

        //Add the receiptId and userId to the store receipts map
        for (String receiptId : receiptIdStoreId.keySet()) {
            storeFacade.addReceiptToStore(receiptIdStoreId.get(receiptId), receiptId, userId);
        }
        return acquisitionId;
    }

    public void paymentFailed(CartDTO cartDTO) throws Exception {
        returnCartToStock(cartDTO.getStoreToProducts());
    }

    //Map<StoreID, Map<ProductName, quantity>>
    public void returnCartToStock(Map<String, Map<String, List<Integer>>> products) throws Exception {
        for (String storeId: products.keySet()){
            storeFacade.returnProductToStore(products.get(storeId), storeId);
        }
    }

    public void logout(String userId) {
        userFacade.logout(userId);
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

        storeFacade.checkQuantityAndPrice(productName, quantity, storeId);
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
        this.roleFacade.createStoreOwnerWithoutAsk(member_ID, store_ID, true, "no nominator");
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


    public void approveStoreOwnerInvitation(String userId,  String storeId) throws Exception {
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.approveInvitationStoreOwner(memberId, storeId);
    }


    public void declineStoreOwnerInvitation(String userId,  String storeId) throws Exception {
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.declineInvitationStoreOwner(memberId, storeId);
    }

    public void declineStoreManagerInvitation(String userId,  String storeId) throws Exception {
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.declineInvitationStoreManager(memberId, storeId);
    }

    public void approveStoreManagerInvitation(String userId,  String storeId) throws Exception {
        String memberId = userFacade.getMemberIdByUserId(userId);
        boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
        if (!succeeded) {
            logout(userId);
            throw new Exception(ExceptionsEnum.sessionOver.toString());
        }
        storeFacade.errorIfStoreNotExist(storeId);
        roleFacade.approveInvitationStoreManager(memberId, storeId);
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
        roleFacade.addOwnerNominator(nominatedMemberID, storeId, false, nominatorMemberID);
    }

    public void fireStoreOwner(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
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
        roleFacade.verifyOwnerNominatorError(nominatorMemberID, nominatedMemberID, storeId);
        roleFacade.fireStoreOwner(nominatedMemberID, storeId);

        myWebSocketHandler.handleStringMessage(nominatedMemberID , "you fired as store owner of store -by " + userFacade.getMemberName(nominatorMemberID));
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
        roleFacade.addManagerNominator(nominatedMemberID, storeId, inventoryPermissions, purchasePermissions, nominatorMemberID);
    }

    public void fireStoreManager(String nominatorUserId, String nominatedUsername, String storeId) throws Exception {
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
        roleFacade.verifyMangerNominatorError(nominatorMemberID, nominatedMemberID, storeId);
        roleFacade.fireStoreManager(nominatedMemberID, storeId);
        myWebSocketHandler.handleStringMessage(nominatedMemberID , "you fired as store owner of store -by " + userFacade.getMemberName(nominatorMemberID));
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
        roleFacade.verifyMangerNominatorError(nominatorMemberID, nominatedMemberID, storeId);
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
        String message = "store " + storeFacade.getStoreName(store_ID) + " closed by " + userFacade.getMemberName(member_ID);
        for (String currMemId : storeManagers) {
            myWebSocketHandler.handleStringMessage(currMemId, message);
        }
        for (String currMemId : storeOwners) {
            myWebSocketHandler.handleStringMessage(currMemId, message);
        }
       /* String storeName = storeFacade.getStoreByID(store_ID).getStoreName();

        Notification n =new StoreNotification(storeName,"The store is now inactive");
        sendMessageToStaffOfStore(n,member_ID);*/
        //todo: update use-case parameters

    }

    public void reopenStore(String user_ID, String store_ID) throws Exception
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
        storeFacade.reopenStore(store_ID);

        sendMessagesToOwnersAndManagers(store_ID , "your store - " + storeFacade.getStoreName(store_ID) +" has been reopened");

    }

 /*   public void sendMessageToStaffOfStore(Notification notification, String member_ID) {
        userFacade.getUserByID(member_ID).notifyObserver(notification);
//       // founder.notifyObserver(notification);
//        for (User u : getOwnersOfStore())
//            u.notifyObserver(notification);
//        for (User u : getManagersOfStore())
//            u.notifyObserver(notification);
    }
    */






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
            storeFacade.checkQuantityAndPrice(productName, quantity, storeId);
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

//    public int checkingCartValidationBeforePurchase(String user_ID, UserDTO userDTO) throws Exception {
//        if (this.userFacade.isMember(user_ID)) {
//            String memberId = this.userFacade.getMemberIdByUserId(user_ID);
//            boolean succeeded = this.authenticationAndSecurityFacade.validateToken(this.authenticationAndSecurityFacade.getToken(memberId));
//            if (!succeeded) {
//                this.logout(user_ID);
//                throw new Exception(ExceptionsEnum.sessionOver.toString());
//            }
//        }
//
//        int totalPrice = 0;
//        this.userFacade.isUserCartEmpty(user_ID);
//        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);
//
//        int storeTotalPrice;
//        for(Iterator storeIt = stores.iterator(); storeIt.hasNext(); totalPrice += storeTotalPrice) {
//            String store_ID = (String)storeIt.next();
//            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
//            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
//            Iterator productIt = products.keySet().iterator();
//
//            String productId;
//            while(productIt.hasNext()) {
//                productId = (String)productIt.next();
//                int quantity = (Integer)((List)products.get(productId)).get(0);
//                this.storeFacade.checkQuantityAndPrice(productId, quantity, store_ID);
//            }
//            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
//            int priceToReduce = this.storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);
//            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
//            storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
//        }
//
//        this.removeUserCartFromStock(user_ID);
//        return totalPrice;
//    }

    public CartDTO checkingCartValidationBeforePurchaseDTO(String user_ID, UserDTO userDTO) throws Exception {
        if (this.userFacade.isMember(user_ID)) {
            String memberId = this.userFacade.getMemberIdByUserId(user_ID);
            boolean succeeded = this.authenticationAndSecurityFacade.validateToken(this.authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                this.logout(user_ID);
                throw new Exception(ExceptionsEnum.sessionOver.toString());
            }
        }

        int totalPrice = 0;
        this.userFacade.isUserCartEmpty(user_ID);
        List<String> stores = this.userFacade.getCartStoresByUser(user_ID);

        int storeTotalPrice;
        for(Iterator storeIt = stores.iterator(); storeIt.hasNext(); totalPrice += storeTotalPrice) {
            String store_ID = (String)storeIt.next();
            Map<String, List<Integer>> products = this.userFacade.getCartProductsByStoreAndUser(store_ID, user_ID);
            List<ProductDTO> productDTOS = this.storeFacade.getProductsDTOSByProductsNames(products, store_ID);
            Iterator productIt = products.keySet().iterator();

            String productId;
            while(productIt.hasNext()) {
                productId = (String)productIt.next();
                int quantity = (Integer)((List)products.get(productId)).get(0);
                this.storeFacade.checkQuantityAndPrice(productId, quantity, store_ID);
            }
            this.storeFacade.checkPurchasePolicy(userDTO, productDTOS, store_ID);
            int priceToReduce = this.storeFacade.calcDiscountPolicy(userDTO, productDTOS, store_ID);
            int storeTotalPriceBeforeDiscount = this.userFacade.getCartPriceByUser(user_ID);
            storeTotalPrice = storeTotalPriceBeforeDiscount - priceToReduce;
        }

        this.removeUserCartFromStock(user_ID);
        return new CartDTO(user_ID, totalPrice, this.getPurchaseList(user_ID));
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

    public void createShiftingDetails(String country, String city, String availibleExteranlSupplyService, String address, String user_ID, String acquisitionId) throws Exception
    {
        String userName = this.userFacade.getUserByID(user_ID).getName();
        if(!supplyServicesFacade.createShiftingDetails(availibleExteranlSupplyService, userName,country,city,address, acquisitionId)){
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
            return Objects.requireNonNull(initializedRepository.findById("market").orElse(null)).isInitialized();
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

    public void addPurchaseRuleToStore(List<TestRuleDTO> testRules, List<String> operators, String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }
        for (TestRuleDTO curr : testRules){
            if (curr.isContains()==null){
                curr.setContains(false);
            }
        }

        checkLogicalRulesAndOperators(testRules, operators);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        //TODO: check testrules
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addPurchaseRuleToStore(testRules, operators, storeId);
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

    public void addDiscountCondRuleToStore(List<TestRuleDTO> testRules, List<String> logicOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators, String userId ,String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
                throw new IllegalArgumentException(ExceptionsEnum.sessionOver.toString());
            }
        }

        checkLogicalRulesAndOperators(testRules, logicOperators);
        checkNumericalRulesAndOperators(discDetails, numericalOperators);
        checkProductDiscountDetails(discDetails);
        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.addDiscountCondRuleToStore(testRules, logicOperators, discDetails, numericalOperators, storeId);
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
            if (discountValueDTO.getPercentage() < 0 || discountValueDTO.getPercentage() > 100) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidDiscountValueParameters.toString());
            }
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

    public void checkLogicalRulesAndOperators(List<TestRuleDTO> ruleNums, List<String> operators) throws Exception {
        if (ruleNums.size() != operators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < operators.size(); i++) {
            if (!operators.get(i).equals("AND") && !operators.get(i).equals("OR") && !operators.get(i).equals("ONLY IF") && !operators.get(i).equals("XOR")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }

    public void checkNumericalRulesAndOperators(List<DiscountValueDTO> discDetails, List<String> numericalOperators) throws Exception {
        if (discDetails.size() != numericalOperators.size() + 1) {
            throw new IllegalArgumentException(ExceptionsEnum.rulesNotMatchOpeators.toString());
        }
        for (int i = 0; i < numericalOperators.size(); i++) {
            if (!numericalOperators.get(i).equals("MAX") && !numericalOperators.get(i).equals("ADDITION")) {
                throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
            }
        }
    }

    public List<String> getStoreCurrentPurchaseRules(String userId, String storeId) throws Exception {
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
        return storeFacade.getStoreCurrentPurchaseRules(storeId);
    }

    public List<String> getStoreCurrentDiscountRules(String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentDiscountRules(storeId);
    }

    public List<AcquisitionDTO> getUserAcquisitionsHistory(String userId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }
        List<String> acquisitions = userFacade.getUserAcquisitionsHistory(userId);
        return paymentServicesFacade.getAcquisitionsDTO(acquisitions);
    }

    public Map<String, ReceiptDTO> getUserReceiptsByAcquisition(String userId, String acquisitionId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }
        //check if user has the acquisition
        userFacade.checkIfUserHasAcquisition(userId, acquisitionId);
        return paymentServicesFacade.getReceiptsDTOByAcquisition(acquisitionId);
    }


    public void composeCurrentPurchaseRules(int ruleIndex1, int ruleIndex2, String operator, String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        if (!operator.equals("AND") && !operator.equals("OR") && !operator.equals("ONLY IF")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentPurchaseRules(ruleIndex1, ruleIndex2, operator, storeId);
    }

    public void composeCurrentSimpleDiscountRules(int ruleIndex1, int ruleIndex2, String numericalOperator, String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        if (!numericalOperator.equals("MAX") && !numericalOperator.equals("ADDITION")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentSimpleDiscountRules(ruleIndex1, ruleIndex2, numericalOperator, storeId);
    }

    public void composeCurrentCondDiscountRules(int ruleIndex1, int ruleIndex2, String logicalOperator, String numericalOperator, String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        if (!logicalOperator.equals("AND") && !logicalOperator.equals("OR") && !logicalOperator.equals("XOR")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }

        if (!numericalOperator.equals("MAX") && !numericalOperator.equals("ADDITION")) {
            throw new IllegalArgumentException(ExceptionsEnum.InvalidOperator.toString());
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        storeFacade.composeCurrentCondDiscountRules(ruleIndex1, ruleIndex2, logicalOperator, numericalOperator, storeId);
    }

    public List<String> getStoreCurrentSimpleDiscountRules(String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentSimpleDiscountRules(storeId);
    }

    public List<String> getStoreCurrentCondDiscountRules(String userId, String storeId) throws Exception {
        if (userFacade.isMember(userId)){
            String memberId = userFacade.getMemberIdByUserId(userId);
            boolean succeeded = authenticationAndSecurityFacade.validateToken(authenticationAndSecurityFacade.getToken(memberId));
            if (!succeeded) {
                logout(userId);
            }
        }

        String member_ID = this.userFacade.getMemberIdByUserId(userId);
        storeFacade.verifyStoreExistError(storeId);
        roleFacade.verifyStoreOwnerError(storeId, member_ID);
        return storeFacade.getStoreCurrentCondDiscountRules(storeId);
    }

    public void checkSystemInitialized() throws Exception {
        if (!isInitialized()){
            throw new Exception("system must initialized first");
        }
    }


}
