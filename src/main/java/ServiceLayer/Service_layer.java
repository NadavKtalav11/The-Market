package ServiceLayer;
import Util.PaymentDTO;
import Util.ProductDTO;
import Util.UserDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import DomainLayer.Market.Market;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Service_layer {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private Market market;

    public Service_layer() {

        this.market = Market.getInstance(); // Initialize the Market instance
    }

    public Service_layer(int i) {
        market = Market.getInstance();
        Market market1 = market.newForTests();
        market=market1;
        // Initialize the Market instance
    }



    public Response<String> init(String userName, String birthday, String country, String city, String address, String name, String password, int licensedDealerNumber,
                                 String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities){
        logger.info("Starting the initialization of the system.");
        try {
            market.init(new UserDTO(userName, birthday, country, city, address, name), password, licensedDealerNumber, paymentServiceName,
                    url, licensedDealerNumber1, supplyServiceName, countries, cities);
            logger.info("System initialized successfully.");
            return new Response<>("Initialization successful", "System initialized successfully.");

        } catch (Exception e) {
            logger.error("Error occurred during the initialization: {}", e.getMessage(), e);
            return new Response<>(null, "Initialization failed: " + e.getMessage());
        }
    }

    public Response<String> addExternalPaymentService(int licensedDealerNumber,String paymentServiceName, String url, String systemMangerId) throws Exception {
        logger.info("Trying to add a new external payment service");
        try {
            market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemMangerId);
            logger.info("Adding new external payment service have been done successfully.");
            return new Response<>("Successful adding", "Adding new external payment service have been done successfully.");

        } catch (Exception e) {
            logger.error("Error occurred during the adding: {}", e.getMessage(), e);
            return new Response<>(null, "Adding failed: " + e.getMessage());
        }
    }

    public Response<String> removeExternalPaymentService(int  licensedDealerNumber, String systemMangerId){
        logger.info("Trying to remove the payment service number: {}",licensedDealerNumber );
        try {
            market.removeExternalPaymentService(licensedDealerNumber,systemMangerId);
            logger.info("Removing the external payment service number: {} have been done successfully.", licensedDealerNumber);
            return new Response<>("Successful adding", "Adding new external payment service have been done successfully.");
        }
        catch (Exception e) {
            logger.error("Error occurred during the removing: {}", e.getMessage(), e);
            return new Response<>(null, "Eemoving failed: " + e.getMessage());
        }

    }

    public Response<String> addExternalSupplyService(int licensedDealerNumber, String supplyServiceName, HashSet<String> countries, HashSet<String> cities, String systemManagerId) throws Exception {
        logger.info("Trying to add a new external supply service");
        try {
            market.addExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities, systemManagerId);
            logger.info("Adding new external supply service has been done successfully.");
            return new Response<>("Successful adding", "Adding new external supply service has been done successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the adding: {}", e.getMessage(), e);
            return new Response<>(null, "Adding failed: " + e.getMessage());
        }
    }

    public Response<String> removeExternalSupplyService(int licensedDealerNumber, String systemManagerId) {
        logger.info("Trying to remove the supply service number: {}", licensedDealerNumber);
        try {
            market.removeExternalSupplyService(licensedDealerNumber, systemManagerId);
            logger.info("Removing the external supply service number: {} has been done successfully.", licensedDealerNumber);
            return new Response<>("Successful removal", "Removing external supply service has been done successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the removing: {}", e.getMessage(), e);
            return new Response<>(null, "Removing failed: " + e.getMessage());
        }
    }

    public Response<String> purchase(String user_ID, String country, String city, String address, String cardNumber, int cvv, int month, int year, String holderID){
        logger.info("Initiating purchase for user: {}", user_ID);
        try {
            market.purchase(user_ID, new PaymentDTO(holderID,cardNumber, cvv, month, year),
                    new UserDTO(null, null, country, city, address, null));
            logger.info("Purchase successful for user: {}", user_ID);
            return new Response<>("Purchase successful", "");
        } catch (Exception e) {
            logger.error("Purchase failed for user: {} with error: {}", user_ID, e.getMessage(), e);
            return new Response<>(null, "Purchase failed: " + e.getMessage());
        }
    }


//    public Response<String> payWithExternalPaymentService(int price, String holderId, String creditCardNumber, int cvv, int month, int year, String userID) {
//        logger.info("Reaching for the payment service in order to complete the purchase.");
//        try {
//            market.payWithExternalPaymentService(price, new PaymentDTO(holderId, creditCardNumber, cvv, month, year),  userID, market.getPurchaseList(userID) );
//            return new Response<>("Successful payment", "payment went successfully.");
//
//        } catch (Exception e) {
//            try {
//                market.paymentFailed(userID);
//            }
//            catch (Exception exception){
//                logger.error("Error occurred while restore stock data: {}", e.getMessage(), e);
//                return new Response<>(null, "restore stock data failed: " + e.getMessage());
//            }
//            logger.error("Error occurred while paying: {}", e.getMessage(), e);
//            return new Response<>(null, "Payment failed: " + e.getMessage());
//        }
//    }


    public Response<String> exitMarketSystem(String userID) {
        logger.info("Exiting market system");
        try {
            market.exitMarketSystem(userID);
            return new Response<>("Exit successful", "User exited the market system successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during exiting market system", e.getMessage(), e);
            return new Response<>(null, "Exit failed: " + e.getMessage());
        }
    }


    public Response<String> enterMarketSystem() {
        logger.info("Entering market system");

        try {
            market.enterMarketSystem();
            return new Response<>("Enter successful", "Entered the market system successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during entering market system", e.getMessage(), e);
            return new Response<>(null, "Enter failed: " + e.getMessage());

        }
    }

    public Response<String> register(String userID, String userName, String birthday, String country, String city, String address, String name, String password) {
        logger.info("Registration");
        try {
            market.register(userID, new UserDTO(userName, birthday, country, city, address, name), password);
            return new Response<>("Registration successful", "User registered successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during registration", e.getMessage(), e);
            return new Response<>(null, "Registration failed: " + e.getMessage());
        }
    }

    public Response<String> login(String userID, String username, String password) {
        logger.info("Log in");

        try {
            market.Login(userID, username, password);
            return new Response<>("Login successful", "User logged in successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during log in", e.getMessage(), e);
            return new Response<>(null, "Login failed: " + e.getMessage());
        }
    }

    public Response<String> logout(String userID) {
        logger.info("Log out");
        try {
            market.logout(userID);
            return new Response<>("Logout successful", "User logged out successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during log out", e.getMessage(), e);
            return new Response<>(null, "Logout failed: " + e.getMessage());
        }
    }

    public Response<String> addProductToStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr) {
        logger.info("Adding product to store");

        try {
            market.addProductToStore(userId, storeID, new ProductDTO(productName, price, quantity, description, categoryStr));
            return new Response<>("Product added successfully", "Product added to store successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding product to store", e.getMessage(), e);
            return new Response<>(null, "Failed to add product: " + e.getMessage());
        }
    }


    public Response<String> removeProductFromStore(String userId, String storeID, String productName) {
        logger.info("Removing product from store");

        try {
            market.removeProductFromStore(userId, storeID, productName);
            return new Response<>("Product removed successfully", "Product removed from store successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during removing product from store", e.getMessage(), e);
            return new Response<>(null, "Failed to remove product: " + e.getMessage());
        }
    }

    public Response<String> updateProductInStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr) {

        logger.info("Updating product in store");
        try {
            market.updateProductInStore(userId, storeID, new ProductDTO(productName, price, quantity, description, categoryStr));
            return new Response<>("Product updated successfully", "Product updated in store successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during updating product in store", e.getMessage(), e);;
            return new Response<>(null, "Failed to update product: " + e.getMessage());
        }
    }

    public Response<String> appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeID) {
        logger.info("AppoString store owner");

        try {
            market.appointStoreOwner(nominatorUserId, nominatedUsername, storeID);
            return new Response<>("Store owner appointed successfully", "Store owner appointed successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during appointing store owner", e.getMessage(), e);
            return new Response<>(null, "Failed to appoString store owner: " + e.getMessage());

        }
    }

    public Response<String> appointStoreManager(String nominatorUserId ,String nominatedUsername, String storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) {

        logger.info("AppoString store manager");

        try {
            market.appointStoreManager(nominatorUserId, nominatedUsername, storeID,
                    inventoryPermissions, purchasePermissions);
            return new Response<>("Store manager appointed successfully", "Store manager appointed successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during appointing store manager", e.getMessage(), e);
            return new Response<>(null, "Failed to appoString store manager: " + e.getMessage());
        }
    }

    public Response<String> updateStoreManagerPermissions(String nominatorUserId ,String nominatedUsername, String storeID,
                                              boolean inventoryPermissions, boolean purchasePermissions) {

        logger.info("Updating store manager permissions");

        try {
            market.updateStoreManagerPermissions(nominatorUserId ,nominatedUsername, storeID,
                    inventoryPermissions, purchasePermissions);
            return new Response<>("Permissions updated successfully", "Store manager permissions updated successfully.");
        } catch (Exception e) {

            logger.error("Error occurred during updating store manager permissions", e.getMessage(), e);
            return new Response<>(null, "Failed to update permissions: " + e.getMessage());

        }
    }

    public Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting general product search filter in the system.");

        try {
            List<String> filteredProductNames = market.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
            return new Response<>(filteredProductNames, "Product filter applied successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the general product search filter: {}", e.getMessage(), e);
            return new Response<>(null, "Product filter failed: " + e.getMessage());
        }
    }

    public Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords)
    {
        logger.info("Starting general product search in the system.");

        try {
            List<String> filteredProductNames = market.generalProductSearch(userId, productName, categoryStr, keywords);
            return new Response<>(filteredProductNames, "Product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the general product search: {}", e.getMessage(), e);
            return new Response<>(null, "Product search failed: " + e.getMessage());
        }
    }

//    public Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String country, String city, String address)
//    {
//        logger.info("Starting care validation and price calculation before purchase.");
//
//        try {
//            int totalPrice = market.checkingCartValidationBeforePurchase(user_ID, country,city,address);
//            return new Response<>(totalPrice, "Cart validation and price calculation completed successfully.");
//        } catch (Exception e) {
//            logger.error("Error occurred during the validation of the cart: {}", e.getMessage(), e);
//            return new Response<>(null, "Cart validation failed: " + e.getMessage());
//        }
//    }


    public Response<List<String>> getInformationAboutStores(String user_ID)
    {
        logger.info("Starting reviewing information about stores in the market.");

        try {
            List<String> allAvailableStores = market.getInformationAboutStores(user_ID);
            return new Response<>(allAvailableStores, "Information about stores retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about stores in the market: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to retrieve information about stores: " + e.getMessage());
        }

    }

    public Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  reviewing information about employees in.");

        try {
            Map<String, String> information = market.getInformationAboutRolesInStore(user_ID, store_ID);
            return new Response<>(information, "Information about roles in store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about products in store: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to retrieve information about roles in store: " + e.getMessage());
        }
    }

    public Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  reviewing authorizations of managers in store.");

        try {
            Map<String, List<Integer>> managersAuthorizations = market.getAuthorizationsOfManagersInStore(user_ID, store_ID);
            return new Response<>(managersAuthorizations, "Authorizations of managers in store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during store owner reviewing authorizations of managers in store: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to retrieve authorizations of managers in store: " + e.getMessage());
        }
    }

    public Response<String> closeStore(String user_ID, String store_ID)
    {
        logger.info("Store owner started  store closing.");

        try {
            market.closeStore(user_ID, store_ID);
            return new Response<>("Store closed successfully", "Store closed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to close a store: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to close store: " + e.getMessage());
        }
    }

    public Response<String> openStore(String user_ID, String name, String description)
    {
        logger.info("Store owner stared store opening.");

        try {
            market.openStore(user_ID, name, description);
            return new Response<>("Store opened successfully", "Store opened successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to open a store: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to open store: " + e.getMessage());
        }
    }

    public Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId)
    {
        logger.info("User try to add a new product to his basket");

        try {
            market.addProductToBasket(productName, quantity, storeId, userId);
            return new Response<>("Product added to basket successfully", "Product added to basket successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during adding new product to the basket: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to add product to basket: " + e.getMessage());
        }
    }

    public Response<String> removeProductFromBasket(String productName, String storeId, String userId)
    {
        logger.info("User try to remove a product from his basket");

        try {
            market.removeProductFromBasket(productName, storeId, userId);
            return new Response<>("Product removed from basket successfully", "Product removed from basket successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during removing a product from the basket: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to remove product from basket: " + e.getMessage());
        }
    }

    public Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId)
    {
        logger.info("User try to modify his shopping cart");

        try {
            market.modifyShoppingCart(productName, quantity, storeId, userId);
            return new Response<>("Shopping cart modified successfully", "Shopping cart modified successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during modifying shopping cart: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to modify shopping cart: " + e.getMessage());
        }
    }

    public Response<Map<String, Integer>> marketManagerAskInfo(String user_ID)
    {
        logger.info("Market manager tries to get info about purchases in the market");

        try {
            Map<String, Integer> marketPurchases = market.marketManagerAskInfo(user_ID);
            return new Response<>(marketPurchases, "Information about purchases in the market retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the request of the market manager getting the purchase information: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to retrieve purchase information: " + e.getMessage());
        }
    }

    public Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID) //return receiptId and total amount in the receipt for the specific store
    {
        logger.info("Store owner tries to get info about purchases in the store");

        try {
            Map<String, Integer> storePurchases = market.storeOwnerGetInfoAboutStore(user_ID, store_ID);
            return new Response<>(storePurchases, "Information about purchases in the store retrieved successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the request of the store owner getting the purchase information: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to retrieve purchase information: " + e.getMessage());
        }
    }

    public Response<List<String>> inStoreProductFilter(String userId ,String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting in-store product search filter in the system.");

        try {
            List<String> filteredProductNames = market.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
            return new Response<>(filteredProductNames, "In-store product search filter applied successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search filter: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to apply in-store product search filter: " + e.getMessage());
        }
    }

    public Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId)
    {
        logger.info("Starting in-store product search in the system.");

        try {
            List<String> filteredProductNames = market.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
            return new Response<>(filteredProductNames, "In-store product search completed successfully.");
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search: {}", e.getMessage(), e);
            return new Response<>(null, "Failed to perform in-store product search: " + e.getMessage());
        }
    }


}