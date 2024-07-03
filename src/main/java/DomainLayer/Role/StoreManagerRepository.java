package DomainLayer.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreManagerRepository extends JpaRepository<StoreManager, String> {
    @Query("SELECT so FROM StoreManager so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId")
    StoreManager get(String storeId, String memberId);

    @Query("SELECT so.id.member_ID FROM StoreManager so")
    List<String> getAllMemberId();

    @Query("SELECT so FROM StoreManager so WHERE so.id.member_ID = :memberId")
    List<StoreManager> getAllMemberIdManagers(String memberId);
}

