package DomainLayer.Store;

import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreRepository {

    public Store get(String  i);
    public void add(String storeId ,Store to_add);
    public void remove(String storeId);
    public List<Store> getAll();
    public boolean contain(String storeId);
    public List<String> getAllIds();
}
