package AcceptanceTests;

import ServiceLayer.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface BridgeToTests {

    Response<String> init(String userName, String password,String birthday, String country, String city, String address, String name, int licensedDealerNumber,
                          String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities);

    Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, int userID);


    Response<String> exitMarketSystem(int userID);

    Response<String> enterMarketSystem();

    Response<String> register(int userID, String username, String password, String birthday,String country, String city, String address, String name);

    Response<String> login(int userID, String username, String password);

    Response<String> logout(int userId);

    Response<String> addProductToStore(int userId, int storeID, String productName, int price, int quantity,
                                       String description, String categoryStr);

    Response<String> removeProductFromStore(int userId, int storeID, String productName);

    Response<String> updateProductInStore(int userId, int storeID, String productName, int price, int quantity,
                                          String description, String categoryStr);

    Response<String> appointStoreOwner(int nominatorUserId, String nominatedUsername, int storeID);

    Response<String> appointStoreManager(int nominatorUserId, String nominatedUsername, int storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions);

    Response<String> updateStoreManagerPermissions(int nominatorUserId, String nominatedUsername, int storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions);

    Response<List<String>> generalProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords);

    Response<Integer> checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address);

    Response<List<Integer>> getInformationAboutStores(int user_ID);

    Response<Map<Integer, String>> getInformationAboutRolesInStore(int user_ID, int store_ID);

    Response<Map<Integer, List<Integer>>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID);

    Response<String> closeStore(int user_ID, int store_ID);

    Response<String> openStore(int user_ID, String name, String description);

    Response<String> addProductToBasket(String productName, int quantity, int storeId, int userId);

    Response<String> removeProductFromBasket(String productName, int storeId, int userId);

    Response<String> modifyShoppingCart(String productName, int quantity, int storeId, int userId);

    Response<Map<Integer, Integer>> marketManagerAskInfo(int user_ID);

    Response<Map<Integer, Integer>> storeOwnerGetInfoAboutStore(int user_ID, int store_ID);

    Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId);
}
