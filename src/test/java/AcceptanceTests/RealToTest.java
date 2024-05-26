package AcceptanceTests;

import ServiceLayer.Response;
import ServiceLayer.Service_layer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RealToTest implements BridgeToTests {
    private Service_layer service;

    public RealToTest()
    {
        this.service = new Service_layer();
    }

    public Response<String> init(String userName, String password,String birthday, String country, String city, String address, String name, int licensedDealerNumber,
                                 String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities)
    {

        return service.init(userName, password, birthday, country, city,address, name,licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, countries, cities);


    }

    @Override
    public Response<String> payWithExternalPaymentService(int price,int cardNumber, int cvv, int month, int year, String holderID, int userID)
    {
        return service.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID);

    }

    public Response<String> exitMarketSystem(int userID)
    {
        return service.exitMarketSystem(userID);
    }

    public Response<String> enterMarketSystem()
    {
        return service.enterMarketSystem();
    }

    public Response<String> register(int userID, String username, String password, String birthday,String country, String city, String address, String name)
    {
        return service.register(userID, username, password, birthday,country, city, address, name);
    }

    public Response<String> login(int userID, String username, String password)
    {
        return service.login(userID, username, password);
    }

    public Response<String> logout(int userId)
    {
        return service.logout(userId);
    }

    @Override
    public Response<String> addProductToStore(int userId, int storeID, String productName, int price, int quantity,
                                       String description, String categoryStr)
    {
        return service.addProductToStore(userId, storeID, productName, price, quantity, description, categoryStr);
    }

    public Response<String> removeProductFromStore(int userId, int storeID, String productName)
    {
        return service.removeProductFromStore(userId, storeID, productName);
    }

    public Response<String> updateProductInStore(int userId, int storeID, String productName, int price, int quantity,
                                          String description, String categoryStr)
    {
        return service.updateProductInStore(userId, storeID, productName, price, quantity, description, categoryStr);
    }

    public Response<String> appointStoreOwner(int nominatorUserId, String nominatorUsername, int storeID)
    {
        return service.appointStoreOwner(nominatorUserId, nominatorUsername, storeID);
    }

    public Response<String> appointStoreManager(int nominatorUserId, String nominatedUsername, int storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.appointStoreManager(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<String> updateStoreManagerPermissions(int nominatorUserId, String nominatedUsername, int storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.updateStoreManagerPermissions(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
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

    public Response<Integer> checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address)
    {
        return service.checkingCartValidationBeforePurchase(user_ID, country, city,address);
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

    public Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating,
                                                int storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        return service.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId)
    {
        return service.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
    }
}
