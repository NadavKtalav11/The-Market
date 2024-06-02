package AcceptanceTests;

import ServiceLayer.Response;
import ServiceLayer.Service_layer;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RealToTest implements BridgeToTests {
    private Service_layer service;

    public RealToTest()
    {
        //this.service = new Service_layer();
        service = new Service_layer(1);

    }

    public Response<String> init(UserDTO user, String password, int licensedDealerNumber,
                                 String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, HashSet<String> countries, HashSet<String> cities)
    {

        return service.init(user, password, licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, countries, cities);


    }

    @Override
    public Response<String> payWithExternalPaymentService(int price,String cardNumber, int cvv, int month, int year, String holderID, String userID)
    {
        return service.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID);

    }

    public Response<String> exitMarketSystem(String userID)
    {
        return service.exitMarketSystem(userID);
    }

    public Response<String> enterMarketSystem()
    {
        return service.enterMarketSystem();
    }

    public Response<String> register(String userID, UserDTO user, String password)
    {
        return service.register(userID, user, password);
    }

    public Response<String> login(String userID, String username, String password)
    {
        return service.login(userID, username, password);
    }

    public Response<String> logout(String userId)
    {
        return service.logout(userId);
    }

    @Override
    public Response<String> addProductToStore(String userId, String storeID, ProductDTO product)
    {
        return service.addProductToStore(userId, storeID, product);
    }

    public Response<String> removeProductFromStore(String userId, String storeID, String productName)
    {
        return service.removeProductFromStore(userId, storeID, productName);
    }

    public Response<String> updateProductInStore(String userId, String storeID, ProductDTO product)
    {
        return service.updateProductInStore(userId, storeID, product);
    }

    public Response<String> appointStoreOwner(String nominatorUserId, String nominatorUsername, String storeID)
    {
        return service.appointStoreOwner(nominatorUserId, nominatorUsername, storeID);
    }

    public Response<String> appointStoreManager(String nominatorUserId, String nominatedUsername, String storeID,
                                         boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.appointStoreManager(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<String> updateStoreManagerPermissions(String nominatorUserId, String nominatedUsername, String storeID,
                                                   boolean inventoryPermissions, boolean purchasePermissions)
    {
        return service.updateStoreManagerPermissions(nominatorUserId, nominatedUsername, storeID, inventoryPermissions, purchasePermissions);
    }

    public Response<List<String>> generalProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating,
                                                List<String> productsFromSearch, Double storeMinRating)
    {
        return service.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> generalProductSearch(String userId, String productName, String categoryStr, List<String> keywords)
    {
        return service.generalProductSearch(userId, productName, categoryStr, keywords);
    }

    public Response<Integer> checkingCartValidationBeforePurchase(String user_ID, String country, String city, String address)
    {
        return service.checkingCartValidationBeforePurchase(user_ID, country, city,address);
    }

    public Response<List<String>> getInformationAboutStores(String user_ID)
    {
        return service.getInformationAboutStores(user_ID);
    }

    public Response<Map<String, String>> getInformationAboutRolesInStore(String user_ID, String store_ID)
    {
        return service.getInformationAboutRolesInStore(user_ID, store_ID);
    }

    public Response<Map<String, List<Integer>>> getAuthorizationsOfManagersInStore(String user_ID, String store_ID)
    {
        return service.getAuthorizationsOfManagersInStore(user_ID, store_ID);
    }

    public Response<String> closeStore(String user_ID, String store_ID)
    {
        return service.closeStore(user_ID, store_ID);
    }

    public Response<String> openStore(String user_ID, String name, String description)
    {

        return service.openStore(user_ID, name, description);
    }

    public Response<String> addProductToBasket(String productName, int quantity, String storeId, String userId)
    {
        return service.addProductToBasket(productName, quantity, storeId, userId);
    }

    public Response<String> removeProductFromBasket(String productName, String storeId, String userId)
    {
        return service.removeProductFromBasket(productName, storeId, userId);
    }

    public Response<String> modifyShoppingCart(String productName, int quantity, String storeId, String userId)
    {
        return service.modifyShoppingCart(productName, quantity, storeId, userId);
    }

    public Response<Map<String, Integer>> marketManagerAskInfo(String user_ID)
    {
        return service.marketManagerAskInfo(user_ID);
    }

    public Response<Map<String, Integer>> storeOwnerGetInfoAboutStore(String user_ID, String store_ID)
    {
        return service.storeOwnerGetInfoAboutStore(user_ID, store_ID);
    }

    public Response<List<String>> inStoreProductFilter(String userId, String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double productMinRating,
                                                String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        return service.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
    }

    public Response<List<String>> inStoreProductSearch(String userId, String productName, String categoryStr, List<String> keywords, String storeId)
    {
        return service.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
    }
}
