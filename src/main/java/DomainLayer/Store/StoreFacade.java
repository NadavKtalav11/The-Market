package DomainLayer.Store;

import java.util.*;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;
    Map<Integer, Store> allStores = new HashMap<Integer, Store>();
    private int currentStoreID;

    private Object allStoresLock;
    private Object storeIdLock;


    private StoreFacade()
    {
        this.currentStoreID = 0;

        allStoresLock = new Object();
        storeIdLock = new Object();

    }

    public synchronized static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }

    public Store getStoreByID(int storeID){
        synchronized (allStoresLock) {
            return allStores.get(storeID);
        }
    }

    public int openStore()
    {
        Store newStore = new Store(currentStoreID); //todo: add this to list in repository
        synchronized (allStoresLock) {
            synchronized (storeIdLock) {
                this.allStores.put(currentStoreID, newStore);
                this.currentStoreID++;
            }
        }
        return newStore.getStoreID();
    }

    public boolean checkQuantityAndPolicies(String productName, int quantity, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException("The product you try to add isn't in the store");
        }

        if (!store.checkProductQuantity(productName, quantity))
        {
            throw new IllegalArgumentException("The quantity you entered isn't available in the store");
        }

        //Check here all policies
        if (!store.checkPurchasePolicy(userId, productName))
        {
            throw new IllegalArgumentException("The product doesnt meet the store purchase policy");
        }

        if (!store.checkDiscountPolicy(userId, productName))
        {
            throw new IllegalArgumentException("The product doesnt meet the store discount policy");
        }

        return true;
    }

    public int calcPrice(String productName, int quantity, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }


    public void addProductToStore(int storeID, String productName, int price, int quantity,
                                                                String description, String categoryStr) {
        synchronized (allStoresLock) {
            allStores.get(storeID).addProduct(productName, price, quantity, description, categoryStr);
        }
    }

    public void removeProductFromStore(int storeID, String productName){
        synchronized (allStoresLock) {
            allStores.get(storeID).removeProduct(productName);
        }
    }

    public void updateProductInStore(int storeID, String productName, int price, int quantity,
                                                                String description, String categoryStr) {
        synchronized (allStoresLock) {
            allStores.get(storeID).updateProduct(productName, price, quantity, description, categoryStr);
        }
    }

    public boolean verifyStoreExist(int storeID)
    {
        return getStoreByID(storeID) != null;
    }

    public void closeStore(int store_ID)
    {
        Store storeToClose = this.getStoreByID(store_ID);
        storeToClose.closeStore();
    }

    public List<Integer> getInformationAboutOpenStores()
    {
        List<Integer> openStoreInformation = new ArrayList<>();
        synchronized (allStoresLock) {
            for (Map.Entry<Integer, Store> entry : allStores.entrySet()) {
                int storeId = entry.getKey();
                Store store = entry.getValue();
                if (store.getIsOpened())
                    openStoreInformation.add(storeId);
            }
        }
        return openStoreInformation;
    }

    public List<Integer> getInformationAboutClosedStores()
    {
        List<Integer> closedStoreInformation = new ArrayList<>();
        synchronized (allStoresLock) {
            for (Map.Entry<Integer, Store> entry : allStores.entrySet()) {
                int storeId = entry.getKey();
                Store store = entry.getValue();
                if (!store.getIsOpened())
                    closedStoreInformation.add(storeId);
            }
        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(int store_ID)
    {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }

    public int calculateTotalCartPriceAfterDiscount(int store_ID, Map<String, List<Integer>> products, int totalPriceBeforeDiscount) {
        return 0; //In the future - check discount and calculate price by policies
    }
    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, int storeId)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.matchProducts(productName, categoryStr, keywords);
        return filteredProducts;
    }

    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, Integer storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.filterProducts(categoryStr, keywords, minPrice, maxPrice, minRating, productsFromSearch, storeMinRating);
        return filteredProducts;
    }

    public void checkCategory(String categoryStr)
    {
        if (Category.fromString(categoryStr) == null)
            throw new IllegalArgumentException("The category you entered doesn't exist.");
    }

    public void checkProductExistInStore(String productName, int storeId)
    {
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException("The product you try to add isn't in the store");
        }
    }

    public List<Integer> getStores() {
        synchronized (allStoresLock) {
            return new ArrayList<>(this.allStores.keySet());
        }
    }

}
