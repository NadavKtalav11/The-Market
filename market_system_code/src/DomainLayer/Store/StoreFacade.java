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
        this.currentStoreID++;
        return newStore.getStoreID();
    }

    public void addProductToStore(int storeID, String productName, int price, int quantity){
        allStores.get(storeID).addProduct(productName, price, quantity);
    }
}
