package DomainLayer.Store;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StoreMemoryRepository implements StoreRepository{

    Map<Integer, Store> allStores ;
    Object storesLock;


    public StoreMemoryRepository(){
        allStores = new HashMap<Integer, Store>();
        storesLock = new Object();

    }


    @Override
    public Store get(int storeID) {
        synchronized (storesLock) {
            return allStores.get(storeID);
        }
    }

    @Override
    public void add(int storeId, Store to_add) {
        synchronized (storesLock){
            allStores.put(storeId, to_add);
        }
    }


    @Override
    public void remove(int storeId) {
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
    public boolean contain(int storeId) {
        return allStores.containsKey(storeId);
    }

    @Override
    public List<Integer> getAllIds() {
        return new ArrayList<>(allStores.keySet());
    }
}
