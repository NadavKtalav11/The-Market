package DomainLayer.Store;

import java.util.List;

public interface StoreRepository {

    public Store get(int i);
    public void add(int storeId ,Store to_add);
    public void remove(int storeId);
    public List<Store> getAll();
    public boolean contain(int storeId);
    public List<Integer> getAllIds();
}
