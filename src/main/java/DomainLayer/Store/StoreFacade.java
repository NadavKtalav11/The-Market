package DomainLayer.Store;

import java.util.*;

public class StoreFacade {
    private static StoreFacade storeFacadeInstance;
    Map<Integer, Store> allStores = new HashMap<Integer, Store>();
    private int currentStoreID;

    private StoreFacade()
    {
        this.currentStoreID = 0;
    }

    public static StoreFacade getInstance() {
        if (storeFacadeInstance == null) {
            storeFacadeInstance = new StoreFacade();
        }
        return storeFacadeInstance;
    }

    public Store getStoreByID(int storeID){
        return allStores.get(storeID);
    }

    public int openStore()
    {
        Store newStore = new Store(currentStoreID); //todo: add this to list in repository
        this.allStores.put(currentStoreID, newStore);
        this.currentStoreID++;
        return newStore.getStoreID();
    }

    public boolean checkQuantityAndPolicies(int productId, int quantity, int storeId, int userId)
    //todo fix this
    {
        Store store = getStoreByID(storeId);
        return store.checkProductQuantity(productId, quantity);

        //Not sure if purchase and discount policies should be checked now
    }

    public void addProductToStore(int storeID, String productName, int price, int quantity){
        allStores.get(storeID).addProduct(productName, price, quantity);
    }

    public void removeProductFromStore(int storeID, String productName){
        allStores.get(storeID).removeProduct(productName);
    }

    public void updateProductInStore(int storeID, String productName, int price, int quantity){
        allStores.get(storeID).updateProduct(productName, price, quantity);
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
        for (Map.Entry<Integer, Store> entry : allStores.entrySet()) {
            int storeId = entry.getKey();
            Store store = entry.getValue();
            if(store.getIsOpened())
                openStoreInformation.add(storeId);
        }
        return openStoreInformation;
    }

    public List<Integer> getInformationAboutClosedStores()
    {
        List<Integer> closedStoreInformation = new ArrayList<>();
        for (Map.Entry<Integer, Store> entry : allStores.entrySet()) {
            int storeId = entry.getKey();
            Store store = entry.getValue();
            if(!store.getIsOpened())
                closedStoreInformation.add(storeId);
        }
        return closedStoreInformation;
    }

    public List<String> getStoreProducts(int store_ID)
    {
        Store store = getStoreByID(store_ID);
        return store.getProducts();
    }
}
