package DomainLayer.Store;

import Util.ExceptionsEnum;
import Util.ProductDTO;

import java.util.*;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;

    private StoreRepository allStores ;



    //private int currentStoreID;
    //private Object allStoresLock;
    //private Object storeIdLock;
    //Map<Integer, Store> allStores = new HashMap<Integer, Store>();


    private StoreFacade()
    {

        allStores = new StoreMemoryRepository();

        //allStoresLock = new Object();
        //storeIdLock = new Object();
        //this.currentStoreID = 0;

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

    public void returnProductToStore(Map<String, Integer> products , String storeId){
        getStoreByID(storeId).returnProductToStore(products);
    }

    public Store getStoreByID(String storeID){
        return allStores.get(storeID);
    }

    public String getNewStoreId(){
        UUID uuid = UUID.randomUUID();
        String storeId = "store" + uuid.toString();
        return storeId;
    }

    public String openStore(String name, String description)
    {
        String storeId = getNewStoreId();
        Store newStore = new Store(storeId, name, description); //todo: add this to list in repository
        this.allStores.add(storeId, newStore);
        return newStore.getStoreID();
    }


    public boolean checkQuantityAndPolicies(String productName, int quantity, String storeId, String userId) {

        this.checkIfProductExists(productName, storeId);
        this.checkProductQuantityAvailability(productName, storeId, quantity);
        this.checkIfProductQuantityIsPositive(quantity);

        //Check here all policies
        this.checkPurchasePolicy(productName, storeId, userId);
        this.checkDiscountPolicy(productName, storeId, userId);
        return true;
        //todo nitzan check merge ;

    }

    public void checkIfProductExists(String productName, String storeId){
        Store store = getStoreByID(storeId);
        if (!store.checkProductExists(productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.productNotExistInStore.toString());
        }
    }

    public void checkProductQuantityAvailability(String productName, String storeId, int quantity)
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

    public void checkPurchasePolicy(String productName, String storeId, String userId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkPurchasePolicy(userId, productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.purchasePolicyIsNotMet.toString());
        }
    }

    public void checkDiscountPolicy(String productName, String storeId, String userId)
    {
        Store store = getStoreByID(storeId);

        if (!store.checkDiscountPolicy(userId, productName))
        {
            throw new IllegalArgumentException(ExceptionsEnum.discountPolicyIsNotMet.toString());
        }
    }

    public int calcPrice(String productName, int quantity, String storeId, String userId)
    {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
    }


    public void addProductToStore(String storeId, ProductDTO product) throws Exception {

        if (!checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.get(storeId).addProduct(product);
            } else {
                throw new Exception("Quantity must be non-negative");
            }
        } else {
            throw new Exception("Product already exist in this store");
        }
    }

    public void removeProductFromStore(String storeId, String productName) throws Exception {
        if (checkProductExistInStore(productName, storeId)) {
            allStores.get(storeId).removeProduct(productName);
        } else {
            throw new Exception("Product does not exist in this store");
        }

    }

    public void updateProductInStore(String storeId, ProductDTO product) throws Exception {

        if (checkProductExistInStore(product.getName(), storeId)) {
            if (product.getQuantity() >= 0) {
                allStores.get(storeId).updateProduct(product);
            } else {
                throw new Exception("Quantity must be non-negative");
            }
        } else {
            throw new Exception("Product does not exist in this store");
        }
    }

    public boolean verifyStoreExist(String storeID)
    {
        return getStoreByID(storeID) != null;
    }

    public void verifyStoreExistError(String storeID) throws Exception {
        if(!verifyStoreExist(storeID))
            throw new Exception(ExceptionsEnum.storeNotExist.toString());
    }

    public void closeStore(String store_ID) throws Exception
    {
        Store storeToClose = this.getStoreByID(store_ID);
        if (!storeToClose.getIsOpened()){
            throw new Exception("store is already closed");
        }
        storeToClose.closeStore();
    }

    public List<String> getInformationAboutOpenStores()
    {
        List<String> openStoreInformation = new ArrayList<>();
            for (Store store : allStores.getAll()) {
                //int storeId = entry.getKey();
                //Store store = entry.getValue();
                if (store.getIsOpened())
                    openStoreInformation.add(store.getStoreID());
            }
        return openStoreInformation;
    }

    public List<String> getInformationAboutClosedStores() {
        List<String> closedStoreInformation = new ArrayList<>();
        for (Store store : allStores.getAll()) {
            //int storeId = entry.getKey();
            //Store store = entry.getValue();
            if (!store.getIsOpened()) {
                closedStoreInformation.add(store.getStoreID());
            }

        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(String store_ID)
    {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }

    public int calculateTotalCartPriceAfterDiscount(String store_ID, Map<String, List<Integer>> products, int totalPriceBeforeDiscount) {
        return totalPriceBeforeDiscount; //In the future - check discount and calculate price by policies
    }
    public List<String> inStoreProductSearch(String productName, String categoryStr, List<String> keywords, String storeId)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.matchProducts(productName, categoryStr, keywords);
        return filteredProducts;
    }

    public List<String> inStoreProductFilter(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, String storeId, List<String> productsFromSearch, Double storeMinRating)
    {
        Store storeToSearchIn = getStoreByID(storeId);
        List<String> filteredProducts = storeToSearchIn.filterProducts(categoryStr, keywords, minPrice, maxPrice, minRating, productsFromSearch, storeMinRating);
        return filteredProducts;
    }

    public boolean checkCategory(String categoryStr)
    {
        return !(Category.fromString(categoryStr) == null);
    }

    public boolean checkProductExistInStore(String productName, String storeId)
    {
        Store store = getStoreByID(storeId);
        return store.checkProductExists(productName);
    }

    public List<String> getStores() {

            return allStores.getAllIds();
        }


    public void addReceiptToStore(String storeId, String  receiptId, String userId)
    {
        allStores.get(storeId).addReceipt(receiptId, userId);
    }

}
