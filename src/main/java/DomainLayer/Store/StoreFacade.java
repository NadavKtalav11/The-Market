package DomainLayer.Store;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    public boolean checkQuantityAndPolicies(String productName, int quantity, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);
        if (!store.checkProductQuantity(productName, quantity))
        {
            throw new IllegalArgumentException("The product you try to add isn't in the store");
        }

        //Check here all policies
        return true;
    }

    public int calcPrice(String productName, int quantity, int storeId, int userId)
    {
        Store store = getStoreByID(storeId);
        return store.calcPriceInStore(productName, quantity, userId);
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
}
