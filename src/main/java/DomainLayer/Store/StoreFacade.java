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

    public void returnProductToStore(Map<String, Integer> products , int storeId){
        getStoreByID(storeId).returnProductToStore(products);
    }

    public Store getStoreByID(int storeID){
        synchronized (allStoresLock) {
            return allStores.get(storeID);
        }
    }

    public int openStore(String name, String description)
    {
        Store newStore = new Store(currentStoreID, name, description); //todo: add this to list in repository
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


    public void addProductToStore(int storeId, String productName, int price, int quantity,
                                                                String description, String categoryStr) throws Exception {
        synchronized (allStoresLock) {
            if (!checkProductExistInStore(productName, storeId)){
                if (quantity >= 0){
                    allStores.get(storeId).addProduct(productName, price, quantity, description, categoryStr);
                } else {
                    throw new Exception("Quantity must be non-negative");
                }
            } else {
                throw new Exception("Product already exist in this store");
            }
        }
    }

    public void removeProductFromStore(int storeId, String productName) throws Exception {
        synchronized (allStoresLock) {
            if (checkProductExistInStore(productName, storeId)) {
                allStores.get(storeId).removeProduct(productName);
            } else {
                throw new Exception("Product does not exist in this store");
            }
        }
    }

    public void updateProductInStore(int storeId, String productName, int price, int quantity,
                                                                String description, String categoryStr) throws Exception {
        synchronized (allStoresLock) {
            if (checkProductExistInStore(productName, storeId)) {
                if (quantity >= 0){
                    allStores.get(storeId).updateProduct(productName, price, quantity, description, categoryStr);
                } else {
                    throw new Exception("Quantity must be non-negative");
                }
            } else {
                throw new Exception("Product does not exist in this store");
            }
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

    public boolean checkProductExistInStore(String productName, int storeId)
    {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<Integer> getStores() {
        synchronized (allStoresLock) {
            return new ArrayList<>(this.allStores.keySet());
        }
    }

    public void addReceiptToStore(int storeId, int receiptId, int userId)
    {
        allStores.get(storeId).addReceipt(receiptId, userId);
    }

}
