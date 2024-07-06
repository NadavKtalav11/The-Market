package DomainLayer.Repositories;

import DomainLayer.Store.Store;
import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreRepository extends JpaRepository<Store, String> {

    @Query("SELECT s.store_ID FROM Store s")
    public List<String> getAllIds();
}