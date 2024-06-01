package DomainLayer.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreMemoryRepository implements StoreRepository{

    private Map<String, Store> allStores ;
    private final Object storesLock;


    public StoreMemoryRepository(){
        allStores = new HashMap<String, Store>();
        storesLock = new Object();

    }


    @Override
    public Store get(String storeID) {
        synchronized (storesLock) {
            return allStores.get(storeID);
        }
    }

    @Override
    public void add(String storeId, Store to_add) {
        synchronized (storesLock){
            allStores.put(storeId, to_add);
        }
    }


    @Override
    public void remove(String storeId) {
        synchronized (storesLock) {
            allStores.remove(storeId);
        }

    }

    @Override
    public List<Store> getAll() {
        synchronized (storesLock) {
            return new ArrayList<Store>(allStores.values());
        }
    }

    @Override
    public boolean contain(String storeId) {
        synchronized (storesLock) {
            return allStores.containsKey(storeId);
        }
    }

    @Override
    public List<String> getAllIds() {
        synchronized (storesLock) {
            return new ArrayList<>(allStores.keySet());
        }
    }
}
