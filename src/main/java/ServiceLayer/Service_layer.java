package ServiceLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class Service_layer {
    private static final Logger logger = LoggerFactory.getLogger(Service_layer.class);
    private Market market;

    public Service_layer() {

        this.market = Market.getInstance(); // Initialize the Market instance
    }


    public void init(String userName, String password, int licensedDealerNumber,
                     String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address) {
        logger.info("Starting the initialization of the system.");
        try {
            market.init(userName, password, licensedDealerNumber, paymentServiceName,
                    url, licensedDealerNumber1, supplyServiceName, address);
        } catch (Exception e) {
            logger.error("Error occurred during the initialization: {}", e.getMessage(), e);
        }

    }

    public void payWithExternalPaymentService(int price, int cvv, int month, int year, String holderID, int userID) {
        logger.info("Reaching for the payment service in order to complete the purchase.");
        try {
            market.payWithExternalPaymentService( price,  cvv,  month,  year,  holderID,  userID);
        } catch (Exception e) {
            logger.error("Error occurred with the payment service company: {}", e.getMessage(), e);
        }
    }

    //todo think about the userID and the purpose of this function.
    public void exitMarketSystem(int userID) {
        try {
            market.exitMarketSystem(userID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //todo think about the userID and the purpose of this function.
    public void enterMarketSystem() {
        try {
            market.enterMarketSystem();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void register(int userID, String username, String password, String birthday, String address) {
        try {
            market.register(userID, username, password, birthday, address);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void login(int userID, String username, String password) {
        try {
            market.Login(userID, username, password);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //todo think about where we get the userID
    public void logout(int memberID) {
        try {
            market.Logout(memberID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void addProductToStore(int memberID, int storeID, String productName, int price, int quantity,
                                  String description, String categoryStr) {
        try {
            market.addProductToStore(memberID, storeID, productName, price, quantity, description, categoryStr);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void removeProductFromStore(int memberID, int storeID, String productName) {
        try {
            market.removeProductFromStore(memberID, storeID, productName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateProductInStore(int memberID, int storeID, String productName, int price, int quantity,
                                     String description, String categoryStr) {
        try {
            market.updateProductInStore(memberID, storeID, productName, price, quantity, description, categoryStr);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void appointStoreOwner(int firstMemberID, int secondMemberID, int storeID) {
        try {
            market.appointStoreOwner(firstMemberID, secondMemberID, storeID);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void appointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                    boolean inventoryPermissions, boolean purchasePermissions) {
        try {
            market.appointStoreManager(firstMemberID, secondMemberID, storeID,
                    inventoryPermissions, purchasePermissions);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                              boolean inventoryPermissions, boolean purchasePermissions) {
        try {
            market.updateStoreManagerPermissions(firstMemberID, secondMemberID, storeID,
                    inventoryPermissions, purchasePermissions);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public List<String> generalProductFilter(String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting general product search filter in the system.");

        List<String> filteredProductNames = null;

        try {
            filteredProductNames = market.generalProductFilter(categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
        } catch (Exception e) {
            logger.error("Error occurred during the general product search filter: {}", e.getMessage(), e);
        }

        return filteredProductNames;
    }

    public List<String> generalProductSearch(String productName, String categoryStr, List<String> keywords)
    {
        logger.info("Starting general product search in the system.");

        List<String> filteredProductNames = null;

        try {
            filteredProductNames = market.generalProductSearch(productName, categoryStr, keywords);
        } catch (Exception e) {
            logger.error("Error occurred during the general product search: {}", e.getMessage(), e);
        }

        return filteredProductNames;
    }

    public int checkingCartValidationBeforePurchase(int user_ID)
    {
        logger.info("Starting care validation and price calculation before purchase.");

        int totalPrice = 0;
        try {
            totalPrice = market.checkingCartValidationBeforePurchase(user_ID);
        } catch (Exception e) {
            logger.error("Error occurred during the validation of the cart: {}", e.getMessage(), e);
        }

        return totalPrice;
    }


    public List<Integer> getInformationAboutStores(int user_ID)
    {
        logger.info("Starting reviewing information about stores in the market.");

        List<Integer> allAvailableStores = null;
        try {
            allAvailableStores = market.getInformationAboutStores(user_ID);
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about stores in the market: {}", e.getMessage(), e);
        }

        return allAvailableStores;
    }

    public List<String> getInformationAboutProductInStore(int user_ID, int store_ID)
    {
        logger.info("Starting reviewing information about products in store.");

        List<String> storeProducts = null;
        try {
            storeProducts = market.getInformationAboutProductInStore(user_ID, store_ID);
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about products in store: {}", e.getMessage(), e);
        }

        return storeProducts;
    }

    public Map<Integer, String> getInformationAboutRolesInStore(int user_ID, int store_ID)
    {
        logger.info("Store owner stared reviewing information about employees in.");

        Map<Integer, String> information = null;
        try {
            information = market.getInformationAboutRolesInStore(user_ID, store_ID);
        } catch (Exception e) {
            logger.error("Error occurred during reviewing information about products in store: {}", e.getMessage(), e);
        }

        return information;
    }

    public Map<Integer, List<Integer>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID)
    {
        logger.info("Store owner stared reviewing authorizations of managers in store.");

        Map<Integer, List<Integer>> managersAuthorizations = null;
        try {
            managersAuthorizations = market.getAuthorizationsOfManagersInStore(user_ID, store_ID);
        } catch (Exception e) {
            logger.error("Error occurred during store owner reviewing authorizations of managers in store: {}", e.getMessage(), e);
        }

        return managersAuthorizations;
    }

    public void closeStore(int user_ID, int store_ID)
    {
        logger.info("Store owner stared store closing.");

        try {
            market.closeStore(user_ID, store_ID);
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to close a store: {}", e.getMessage(), e);
        }
    }

    public void openStore(int user_ID)
    {
        logger.info("Store owner stared store opening.");

        try {
            market.openStore(user_ID);
        } catch (Exception e) {
            logger.error("Error occurred during store owner was trying to open a store: {}", e.getMessage(), e);
        }
    }

    public void addProductToBasket(String productName, int quantity, int storeId, int userId)
    {
        logger.info("User try to add a new product to his basket");

        try {
            market.addProductToBasket(productName, quantity, storeId, userId);
        } catch (Exception e) {
            logger.error("Error occurred during adding new product to the basket: {}", e.getMessage(), e);
        }
    }

    public void removeProductFromBasket(String productName, int storeId, int userId)
    {
        logger.info("User try to remove a product from his basket");

        try {
            market.removeProductFromBasket(productName, storeId, userId);
        } catch (Exception e) {
            logger.error("Error occurred during removing a product from the basket: {}", e.getMessage(), e);
        }
    }

    public void modifyShoppingCart(String productName, int quantity, int storeId, int userId)
    {
        logger.info("User try to modify his shopping cart");

        try {
            market.modifyShoppingCart(productName, quantity, storeId, userId);
        } catch (Exception e) {
            logger.error("Error occurred during modifying shopping cart: {}", e.getMessage(), e);
        }
    }

    public Map<Integer, Integer> marketManagerAskInfo(int user_ID)
    {
        logger.info("Market manager tries to get info about purchases in the market");

        Map<Integer, Integer> marketPurchases = null;
        try {
            marketPurchases = market.marketManagerAskInfo(user_ID);
        } catch (Exception e) {
            logger.error("Error occurred during the request of the market manager getting the purchase information: {}", e.getMessage(), e);
        }

        return marketPurchases;
    }

    public Map<Integer, Integer> storeOwnerGetInfoAboutStore(int user_ID, int store_ID) throws Exception //return receiptId and total amount in the receipt for the specific store
    {
        logger.info("Store owner tries to get info about purchases in the store");

        Map<Integer, Integer> storePurchases = null;
        try {
            storePurchases = market.storeOwnerGetInfoAboutStore(user_ID, store_ID);
        } catch (Exception e) {
            logger.error("Error occurred during the request of the store owner getting the purchase information: {}", e.getMessage(), e);
        }

        return storePurchases;
    }

    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        logger.info("Starting in-store product search filter in the system.");

        List<String> filteredProductNames = null;

        try {
            filteredProductNames = market.inStoreProductFilter(categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search filter: {}", e.getMessage(), e);
        }

        return filteredProductNames;
    }

    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, int storeId)
    {
        logger.info("Starting in-store product search in the system.");

        List<String> filteredProductNames = null;

        try {
            filteredProductNames = market.inStoreProductSearch(productName, categoryStr, keywords, storeId);
        } catch (Exception e) {
            logger.error("Error occurred during the in-store product search: {}", e.getMessage(), e);
        }

        return filteredProductNames;
    }


}