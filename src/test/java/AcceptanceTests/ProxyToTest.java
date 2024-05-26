package AcceptanceTests;

import ServiceLayer.Response;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ProxyToTest implements BridgeToTests {

    private RealToTest realServiceAdaptor;

    public ProxyToTest(String type) {
        if (type.equals("Real"))
            this.realServiceAdaptor = new RealToTest();
        else
            this.realServiceAdaptor = null;
    }


    @Override
    public Response<String> init(String userName, String password,String birthday, String country, String city, String address, String name, int licensedDealerNumber,
                                 String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities)
     {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.init(userName, password, birthday, country, city, address, name,licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, countries, cities);

        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> payWithExternalPaymentService(int price,int cardNumber, int cvv, int month, int year, String holderID, int userID)
    {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID);

        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> exitMarketSystem(int userID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.exitMarketSystem(userID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> enterMarketSystem() {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.enterMarketSystem();
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> register(int userID, String username, String password, String birthday,String country, String city, String address, String name) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.register(userID, username, password, birthday, country, city, address, name);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> login(int userID, String username, String password) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.login(userID, username, password);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> logout(int userID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.logout(userID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> addProductToStore(int userId, int storeID, String productName, int price, int quantity, String description, String categoryStr) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.addProductToStore(userId, storeID, productName, price, quantity, description, categoryStr);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> removeProductFromStore(int userId, int storeID, String productName) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.removeProductFromStore(userId, storeID, productName);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> updateProductInStore(int userId, int storeID, String productName, int price, int quantity, String description, String categoryStr) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.updateProductInStore(userId, storeID, productName, price, quantity, description, categoryStr);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> appointStoreOwner(int nominatorUserID, String nominatedUsername, int storeID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.appointStoreOwner(nominatorUserID, nominatedUsername, storeID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> appointStoreManager(int nominatorUserID, String nominatedUsername, int storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.appointStoreManager(nominatorUserID, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> updateStoreManagerPermissions(int nominatorUserID, String nominatedUsername, int storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.updateStoreManagerPermissions(nominatorUserID, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> generalProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.generalProductSearch(userId, productName, categoryStr, keywords);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Integer> checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.checkingCartValidationBeforePurchase(user_ID, country,city,address);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<Integer>> getInformationAboutStores(int user_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getInformationAboutStores(user_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> getInformationAboutProductInStore(int user_ID, int store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getInformationAboutProductInStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<Integer, String>> getInformationAboutRolesInStore(int user_ID, int store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getInformationAboutRolesInStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<Integer, List<Integer>>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getAuthorizationsOfManagersInStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> closeStore(int user_ID, int store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.closeStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> openStore(int user_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.openStore(user_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> addProductToBasket(String productName, int quantity, int storeId, int userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.addProductToBasket(productName, quantity, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> removeProductFromBasket(String productName, int storeId, int userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.removeProductFromBasket(productName, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> modifyShoppingCart(String productName, int quantity, int storeId, int userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.modifyShoppingCart(productName, quantity, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<Integer, Integer>> marketManagerAskInfo(int user_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.marketManagerAskInfo(user_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<Integer, Integer>> storeOwnerGetInfoAboutStore(int user_ID, int store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.storeOwnerGetInfoAboutStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
        else
            return new Response<>(null, "Not Implemented yet");
    }
}
