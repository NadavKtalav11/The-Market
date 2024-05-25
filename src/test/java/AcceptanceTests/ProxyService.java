package AcceptanceTests;

import ServiceLayer.Response;

import java.util.List;
import java.util.Map;

public class ProxyService implements BridgeService {

    private RealServiceAdaptor realServiceAdaptor;

    public ProxyService()
    {
        this.realServiceAdaptor = new RealServiceAdaptor();
    }

    @Override
    public Response<String> init(String userName, String password, int licensedDealerNumber, String paymentServiceName, String url, int licensedDealerNumber1, String supplyServiceName, String address) {
        return realServiceAdaptor.init(userName, password, licensedDealerNumber, paymentServiceName, url, licensedDealerNumber1, supplyServiceName, address);
    }

    @Override
    public Response<String> exitMarketSystem(int userID) {
        return realServiceAdaptor.exitMarketSystem(userID);
    }

    @Override
    public Response<String> enterMarketSystem() {
        return realServiceAdaptor.enterMarketSystem();
    }

    @Override
    public Response<String> register(int userID, String username, String password, String birthday,String country, String city, String address, String name) {
        return realServiceAdaptor.register(userID, username, password, birthday, country, city, address, name);
    }

    @Override
    public Response<String> login(int userID, String username, String password) {
        return realServiceAdaptor.login(userID, username, password);
    }

    @Override
    public Response<String> logout(int memberID) {
        return realServiceAdaptor.logout(memberID);
    }

    @Override
    public Response<String> addProductToStore(int memberID, int storeID, String productName, int price, int quantity, String description, String categoryStr) {
        return realServiceAdaptor.addProductToStore(memberID, storeID, productName, price, quantity, description, categoryStr);
    }

    @Override
    public Response<String> removeProductFromStore(int memberID, int storeID, String productName) {
        return realServiceAdaptor.removeProductFromStore(memberID, storeID, productName);
    }

    @Override
    public Response<String> updateProductInStore(int memberID, int storeID, String productName, int price, int quantity, String description, String categoryStr) {
        return realServiceAdaptor.updateProductInStore(memberID, storeID, productName, price, quantity, description, categoryStr);
    }

    @Override
    public Response<String> appointStoreOwner(int firstMemberID, int secondMemberID, int storeID) {
        return realServiceAdaptor.appointStoreOwner(firstMemberID, secondMemberID, storeID);
    }

    @Override
    public Response<String> appointStoreManager(int firstMemberID, int secondMemberID, int storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        return realServiceAdaptor.appointStoreManager(firstMemberID, secondMemberID, storeID, inventoryPermissions, purchasePermissions);
    }

    @Override
    public Response<String> updateStoreManagerPermissions(int firstMemberID, int secondMemberID, int storeID, boolean inventoryPermissions, boolean purchasePermissions) {
        return realServiceAdaptor.updateStoreManagerPermissions(firstMemberID, secondMemberID, storeID, inventoryPermissions, purchasePermissions);
    }

    @Override
    public Response<List<String>> generalProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, List<String> productsFromSearch, Double storeMinRating) {
        return realServiceAdaptor.generalProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, productsFromSearch, storeMinRating);
    }

    @Override
    public Response<List<String>> generalProductSearch(int userId, String productName, String categoryStr, List<String> keywords) {
        return realServiceAdaptor.generalProductSearch(userId, productName, categoryStr, keywords);
    }

    @Override
    public Response<Integer> checkingCartValidationBeforePurchase(int user_ID, String country, String city, String address) {
        return realServiceAdaptor.checkingCartValidationBeforePurchase(user_ID, country, city, address);
    }

    @Override
    public Response<List<Integer>> getInformationAboutStores(int user_ID) {
        return realServiceAdaptor.getInformationAboutStores(user_ID);
    }

    @Override
    public Response<List<String>> getInformationAboutProductInStore(int user_ID, int store_ID) {
        return realServiceAdaptor.getInformationAboutProductInStore(user_ID, store_ID);
    }

    @Override
    public Response<Map<Integer, String>> getInformationAboutRolesInStore(int user_ID, int store_ID) {
        return realServiceAdaptor.getInformationAboutRolesInStore(user_ID, store_ID);
    }

    @Override
    public Response<Map<Integer, List<Integer>>> getAuthorizationsOfManagersInStore(int user_ID, int store_ID) {
        return realServiceAdaptor.getAuthorizationsOfManagersInStore(user_ID, store_ID);
    }

    @Override
    public Response<String> closeStore(int user_ID, int store_ID) {
        return realServiceAdaptor.closeStore(user_ID, store_ID);
    }

    @Override
    public Response<String> openStore(int user_ID) {
        return realServiceAdaptor.openStore(user_ID);
    }

    @Override
    public Response<String> addProductToBasket(String productName, int quantity, int storeId, int userId) {
        return realServiceAdaptor.addProductToBasket(productName, quantity, storeId, userId);
    }

    @Override
    public Response<String> removeProductFromBasket(String productName, int storeId, int userId) {
        return realServiceAdaptor.removeProductFromBasket(productName, storeId, userId);
    }

    @Override
    public Response<String> modifyShoppingCart(String productName, int quantity, int storeId, int userId) {
        return realServiceAdaptor.modifyShoppingCart(productName, quantity, storeId, userId);
    }

    @Override
    public Response<Map<Integer, Integer>> marketManagerAskInfo(int user_ID) {
        return realServiceAdaptor.marketManagerAskInfo(user_ID);
    }

    @Override
    public Response<Map<Integer, Integer>> storeOwnerGetInfoAboutStore(int user_ID, int store_ID) {
        return realServiceAdaptor.storeOwnerGetInfoAboutStore(user_ID, store_ID);
    }

    @Override
    public Response<List<String>> inStoreProductFilter(int userId, String categoryStr, List<String> keywords, int minPrice, int maxPrice, Double productMinRating, int storeId, List<String> productsFromSearch, Double storeMinRating) {
        return realServiceAdaptor.inStoreProductFilter(userId, categoryStr, keywords, minPrice, maxPrice, productMinRating, storeId, productsFromSearch, storeMinRating);
    }

    @Override
    public Response<List<String>> inStoreProductSearch(int userId, String productName, String categoryStr, List<String> keywords, int storeId) {
        return realServiceAdaptor.inStoreProductSearch(userId, productName, categoryStr, keywords, storeId);
    }
}
