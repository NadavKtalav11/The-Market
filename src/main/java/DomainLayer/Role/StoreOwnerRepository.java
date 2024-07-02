package DomainLayer.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, String> {
    @Query("SELECT so FROM StoreOwner so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId")
    StoreOwner get(String storeId, String memberId);

    @Query("SELECT so.id.member_ID FROM StoreOwner so")
    List<String> getAllMemberId();

    @Query("SELECT so FROM StoreOwner so WHERE so.id.member_ID = :memberId")
    List<StoreOwner> getAllMemberIdOwners(String memberId);
}
