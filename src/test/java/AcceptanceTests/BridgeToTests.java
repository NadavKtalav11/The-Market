package AcceptanceTests;

import ServiceLayer.Response;

import java.util.List;
import java.util.Map;

public interface BridgeToTests {

    Response<String> init(String userName, String password, int licensedDealerNumber,
                          String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address);

    Response<String> exitMarketSystem(int userID);

    Response<String> enterMarketSystem();

    Response<String> register(int userID, String username, String password, String birthday,String country, String city, String address, String name);

    Response<String> login(int userID, String username, String password);

    Response<String> logout(int memberID);

    Response<String> addProductToStore(int memberID, int storeID, String productName, int price, int quantity,
                                       String description, String categoryStr);

    Response<String> removeProductFromStore(int memberID, int storeID, String productName);

    Response<String> updateProductInStore(int memberID, int storeID, String productName, int price, int quantity,
                                          String description, String categoryStr);

    Response<String> appointStoreOwner(int firstMemberID, int secondMemberID, int storeID);

    Response<String> appointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions);

    Response<String> updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions);

    Response<List<String>> generalProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords);

    Response<Integer> checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address);

    Response<List<Integer>> getInformationAboutStores(int user_ID);

    Response<List<String>> getInformationAboutProductInStore(int user_ID, int store_ID);

    Response<Map<Integer, String>> getInformationAboutRolesInStore(int user_ID, int store_ID);

    Response<Map<Integer, List<Integer>>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID);

    Response<String> closeStore(int user_ID, int store_ID);

    Response<String> openStore(int user_ID);

    Response<String> addProductToBasket(String productName, int quantity, int storeId, int userId);

    Response<String> removeProductFromBasket(String productName, int storeId, int userId);

    Response<String> modifyShoppingCart(String productName, int quantity, int storeId, int userId);

    Response<Map<Integer, Integer>> marketManagerAskInfo(int user_ID);

    Response<Map<Integer, Integer>> storeOwnerGetInfoAboutStore(int user_ID, int store_ID);

    Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId);
}
