package AcceptanceTests;

import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;

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
    public Response<String> init(UserDTO user, String password, int licensedDealerNumber,
                                 String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities)
    {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.init(user, password,licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, countries, cities);

        else
            return new Response<>(null, "Not Implemented yet");
    }

//    @Override
//    public Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, String userID)
//    {
//        if (realServiceAdaptor != null)
//            return realServiceAdaptor.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID);
//
//        else
//            return new Response<>(null, "Not Implemented yet");
//    }

    @Override
    public Response<String> exitMarketSystem(String userID) {
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
    public Response<String> register(String userID, UserDTO user, String password) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.register(userID, user, password);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> login(String userID, String username, String password) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.login(userID, username, password);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> logout(String userID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.logout(userID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> addProductToStore(String userId, String storeID, ProductDTO product) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.addProductToStore(userId, storeID, product);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> removeProductFromStore(String userId, String storeID, String productName) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.removeProductFromStore(userId, storeID, productName);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> updateProductInStore(String userId, String storeID, ProductDTO product) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.updateProductInStore(userId, storeID, product);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> appointStoreOwner(String nominatorUserID, String nominatedUsername, String storeID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.appointStoreOwner(nominatorUserID, nominatedUsername, storeID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> appointStoreManager(String nominatorUserID, String nominatedUsername, String storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.appointStoreManager(nominatorUserID, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> updateStoreManagerPermissions(String nominatorUserID, String nominatedUsername, String storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.updateStoreManagerPermissions(nominatorUserID, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.generalProductSearch(userId, productName, categoryStr, keywords);
        else
            return new Response<>(null, "Not Implemented yet");
    }

//    @Override
//    public Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String country, String city, String address) {
//        if (realServiceAdaptor != null)
//            return realServiceAdaptor.checkingCartValidationBeforePurchase(user_ID, country,city,address);
//        else
//            return new Response<>(null, "Not Implemented yet");
//    }

    @Override
    public Response<List<String>> getInformationAboutStores(String user_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getInformationAboutStores(user_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getInformationAboutRolesInStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.getAuthorizationsOfManagersInStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> closeStore(String user_ID, String store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.closeStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> openStore(String user_ID, String name, String description) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.openStore(user_ID, name, description);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.addProductToBasket(productName, quantity, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> removeProductFromBasket(String productName, String storeId, String userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.removeProductFromBasket(productName, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.modifyShoppingCart(productName, quantity, storeId, userId);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<String, Integer>> marketManagerAskInfo(String user_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.marketManagerAskInfo(user_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.storeOwnerGetInfoAboutStore(user_ID, store_ID);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating, String storeId, List<String> productsFromSearch, Double storeMinRating) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
        else
            return new Response<>(null, "Not Implemented yet");
    }

    @Override
    public Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId) {
        if (realServiceAdaptor != null)
            return realServiceAdaptor.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
        else
            return new Response<>(null, "Not Implemented yet");
    }
}
