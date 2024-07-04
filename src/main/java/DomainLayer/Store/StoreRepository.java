package DomainLayer.Store;

import DomainLayer.Role.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;
import jakarta.persistence.*;

@NoRepositoryBean
public interface StoreRepository extends JpaRepository<Store, String> {

    @Query("SELECT s.store_ID FROM Store s")
    List<String> getAllIds();
}
