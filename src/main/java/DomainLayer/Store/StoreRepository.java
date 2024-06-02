package DomainLayer.Store;

import java.util.List;

public interface StoreRepository {

    public Store get(String  i);
    public void add(String storeId ,Store to_add);
    public void remove(String storeId);
    public List<Store> getAll();
    public boolean contain(String storeId);
    public List<String> getAllIds();
}
