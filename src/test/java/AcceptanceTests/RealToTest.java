package AcceptanceTests;

import ServiceLayer.Response;
import ServiceLayer.Service_layer;

import java.util.List;
import java.util.Map;

public class RealToTest implements BridgeToTests {
    private Service_layer service;

    public RealToTest()
    {
        this.service = new Service_layer();
    }

    public Response<String> init(String userName, String password, int licensedDealerNumber,
                          String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address)
    {
        return service.init(userName, password, licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, address);
    }

    public Response<String> exitMarketSystem(int userID)
    {
        return service.exitMarketSystem(userID);
    }

    public Response<String> enterMarketSystem()
    {
        return service.enterMarketSystem();
    }

    public Response<String> register(int userID, String username, String password, String birthday, String address)
    {
        return service.register(userID, username, password, birthday, address);
    }

    public Response<String> login(int userID, String username, String password)
    {
        return service.login(userID, username, password);
    }

    public Response<String> logout(int memberID)
    {
        return service.logout(memberID);
    }

    public Response<String> addProductToStore(int memberID, int storeID, String productName, int price, int quantity,
                                       String description, String categoryStr)
    {
        return service.addProductToStore(memberID, storeID, productName, price, quantity, description, categoryStr);
    }

    public Response<String> removeProductFromStore(int memberID, int storeID, String productName)
    {
        return service.removeProductFromStore(memberID, storeID, productName);
    }

    public Response<String> updateProductInStore(int memberID, int storeID, String productName, int price, int quantity,
                                          String description, String categoryStr)
    {
        return service.updateProductInStore(memberID, storeID, productName, price, quantity, description, categoryStr);
    }

    public Response<String> appointStoreOwner(int firstMemberID, int secondMemberID, int storeID)
    {
        return service.appointStoreOwner(firstMemberID, secondMemberID, storeID);
    }

    public Response<String> appointStoreManager(int firstMemberID, int secondMemberID, int storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.appointStoreManager(firstMemberID, secondMemberID, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<String> updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.updateStoreManagerPermissions(firstMemberID, secondMemberID, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<List<String>> generalProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating,
                                                List<String> productsFromSearch, Double storeMinRating)
    {
        return service.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords)
    {
        return service.generalProductSearch(userId, productName, categoryStr, keywords);
    }

    public Response<Integer> checkingCartValidationBeforePurchase(int user_ID)
    {
        return service.checkingCartValidationBeforePurchase(user_ID);
    }

    public Response<List<Integer>> getInformationAboutStores(int user_ID)
    {
        return service.getInformationAboutStores(user_ID);
    }

    public Response<List<String>> getInformationAboutProductInStore(int user_ID, int store_ID)
    {
        return service.getInformationAboutProductInStore(user_ID, store_ID);
    }

    public Response<Map<Integer, String>> getInformationAboutRolesInStore(int user_ID, int store_ID)
    {
        return service.getInformationAboutRolesInStore(user_ID, store_ID);
    }

    public Response<Map<Integer, List<Integer>>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID)
    {
        return service.getAuthorizationsOfManagersInStore(user_ID, store_ID);
    }

    public Response<String> closeStore(int user_ID, int store_ID)
    {
        return service.closeStore(user_ID, store_ID);
    }

    public Response<String> openStore(int user_ID)
    {
        return service.openStore(user_ID);
    }

    public Response<String> addProductToBasket(String productName, int quantity, int storeId, int userId)
    {
        return service.addProductToBasket(productName, quantity, storeId, userId);
    }

    public Response<String> removeProductFromBasket(String productName, int storeId, int userId)
    {
        return service.removeProductFromBasket(productName, storeId, userId);
    }

    public Response<String> modifyShoppingCart(String productName, int quantity, int storeId, int userId)
    {
        return service.modifyShoppingCart(productName, quantity, storeId, userId);
    }

    public Response<Map<Integer, Integer>> marketManagerAskInfo(int user_ID)
    {
        return service.marketManagerAskInfo(user_ID);
    }

    public Response<Map<Integer, Integer>> storeOwnerGetInfoAboutStore(int user_ID, int store_ID)
    {
        return service.storeOwnerGetInfoAboutStore(user_ID, store_ID);
    }

    public Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating,
                                                int storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        return service.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId)
    {
        return service.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
    }
}