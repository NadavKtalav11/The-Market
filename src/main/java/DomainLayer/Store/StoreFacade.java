package DomainLayer.Store;

import Util.ExceptionsEnum;

import java.util.*;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;
    //Map<Integer, Store> allStores = new HashMap<Integer, Store>();
    private StoreRepository allStores ;
    private int currentStoreID;
    //private Object allStoresLock;
    private Object storeIdLock;


    private StoreFacade()
    {
        this.currentStoreID = 0;
        allStores = new StoreMemoryRepository();

        //allStoresLock = new Object();
        storeIdLock = new Object();

    }

    public synchronized static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }

    public StoreFacade newForTest(){
        storeFacadeInstance= new StoreFacade();
        return storeFacadeInstance;
    }

    public void returnProductToStore(Map<String, Integer> products , int storeId){
        getStoreByID(storeId).returnProductToStore(products);
    }

    public Store getStoreByID(int storeID){
        return allStores.get(storeID);
    }

    public int openStore(String name, String description)
    {
        Store newStore = new Store(currentStoreID, name, description); //todo: add this to list in repository
        this.allStores.add(currentStoreID, newStore);
        synchronized (storeIdLock) {
            this.currentStoreID++;
        }
        return newStore.getStoreID();
    }

    public boolean checkQuantityAndPolicies(String productName, int quantity, int storeId, int userId) {
        this.checkIfProductExists(productName, storeId);
        this.checkProductQuantityAvailability(productName, storeId, quantity);
        this.checkIfProductQuantityIsPositive(quantity);

        //Check here all policies
        this.checkPurchasePolicy(productName, storeId, userId);
        this.checkDiscountPolicy(productName, storeId, userId);

        return true;
    }

    public void checkIfProductExists(String productName, int storeId){
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productNotExist.toString());
        }
    }

    public void checkProductQuantityAvailability(String productName, int storeId, int quantity)
    {
        Store store = getStoreByID(storeId);
        if (!store.checkProductQuantity(productName, quantity))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityNotExist.toString());
        }
    }

    public void checkIfProductQuantityIsPositive(int quantity)
    {
        if (quantity < 0)
        {
            throw new IllegalArgumentException(ExceptionsEnum.productQuantityIsNegative.toString());
        }
    }

    public void checkPurchasePolicy(String productName, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkPurchasePolicy(userId, productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.purchasePolicyIsNotMet.toString());
        }
    }

    public void checkDiscountPolicy(String productName, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkDiscountPolicy(userId, productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.discountPolicyIsNotMet.toString());
        }
    }

    public int calcPrice(String productName, int quantity, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }


    public void addProductToStore(int storeId, String productName, int price, int quantity,
                                                                String description, String categoryStr) throws Exception {

        if (!checkProductExistInStore(productName, storeId)) {
            if (quantity >= 0) {
                allStores.get(storeId).addProduct(productName, price, quantity, description, categoryStr);
            } else {
                throw new Exception("Quantity must be non-negative");
            }
        } else {
            throw new Exception("Product already exist in this store");
        }
    }

    public void removeProductFromStore(int storeId, String productName) throws Exception {
        if (checkProductExistInStore(productName, storeId)) {
            allStores.get(storeId).removeProduct(productName);
        } else {
            throw new Exception("Product does not exist in this store");
        }

    }

    public void updateProductInStore(int storeId, String productName, int price, int quantity,
                                                                String description, String categoryStr) throws Exception {

        if (checkProductExistInStore(productName, storeId)) {
            if (quantity >= 0) {
                allStores.get(storeId).updateProduct(productName, price, quantity, description, categoryStr);
            } else {
                throw new Exception("Quantity must be non-negative");
            }
        } else {
            throw new Exception("Product does not exist in this store");
        }
    }



    public boolean verifyStoreExist(int storeID)
    {
        return getStoreByID(storeID) != null;
    }

    public void closeStore(int store_ID) throws Exception
    {
        Store storeToClose = this.getStoreByID(store_ID);
        if (!storeToClose.getIsOpened()){
            throw new Exception("store is already closed");
        }
        storeToClose.closeStore();
    }

    public List<Integer> getInformationAboutOpenStores()
    {
        List<Integer> openStoreInformation = new ArrayList<>();
            for (Store store : allStores.getAll()) {
                //int storeId = entry.getKey();
                //Store store = entry.getValue();
                if (store.getIsOpened())
                    openStoreInformation.add(store.getStoreID());
            }
        return openStoreInformation;
    }

    public List<Integer> getInformationAboutClosedStores() {
        List<Integer> closedStoreInformation = new ArrayList<>();
        for (Store store : allStores.getAll()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (!store.getIsOpened()) {
                closedStoreInformation.add(store.getStoreID());
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
        return totalPriceBeforeDiscount; //In the future - check discount and calculate price by policies
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

    public boolean checkCategory(String categoryStr)
    {
        return !(Category.fromString(categoryStr) == null);
    }

    public boolean checkProductExistInStore(String productName, int storeId)
    {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<Integer> getStores() {

            return allStores.getAllIds();
        }


    public void addReceiptToStore(int storeId, int receiptId, int userId)
    {
        allStores.get(storeId).addReceipt(receiptId, userId);
    }

}
