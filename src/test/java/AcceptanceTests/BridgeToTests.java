package AcceptanceTests;

import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public interface BridgeToTests {

    Response<String> init(String userName, String birthday, String country, String city, String address, String name, String password, int licensedDealerNumber,
                          String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities);

//    Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, String userID);


    Response<String> exitMarketSystem(String userID);

    Response<String> enterMarketSystem();

    Response<String> register(String userID, String userName, String birthdate, String country, String city, String address, String name, String password);

    Response<String> login(String userID, String username, String password);

    Response<String> logout(String userId);

    Response<String> addProductToStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr);

    Response<String> removeProductFromStore(String userId, String storeID, String productName);

    Response<String> updateProductInStore(String userId, String storeID, String productName, int price, int quantity, String description, String categoryStr);

    Response<String> appointStoreOwner(String nominatorUserId, String nominatedUsername, String storeID);

    Response<String> appointStoreManager(String nominatorUserId, String nominatedUsername, String storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions);

    Response<String> updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions);

    Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords);

//    Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String country, String city, String address);

    Response<List<String>> getInformationAboutStores(String user_ID);

    Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID);

    Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID);

    Response<String> closeStore(String user_ID, String store_ID);

    Response<String> openStore(String user_ID, String name, String description);

    Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId);

    Response<String> removeProductFromBasket(String productName, String storeId, String userId);

    Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId);

    Response<Map<String, Integer>> marketManagerAskInfo(String user_ID);

    Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID);

    Response<List<String>> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating);

    Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId);
}
