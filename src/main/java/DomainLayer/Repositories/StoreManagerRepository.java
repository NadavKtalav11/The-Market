package DomainLayer.Repositories;

import DomainLayer.Role.StoreManager;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import java.util.List;

@NoRepositoryBean
public interface StoreManagerRepository extends JpaRepository<StoreManager, String> {
    @Query("SELECT so FROM StoreManager so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId")
    StoreManager get(String storeId, String memberId);

    @Query("SELECT so.id.member_ID FROM StoreManager so")
    List<String> getAllMemberId();

    @Query("SELECT so FROM StoreManager so WHERE so.id.member_ID = :memberId")
    List<StoreManager> getAllMemberIdManagers(String memberId);

    @Transactional
    @Modifying
    @Query("UPDATE StoreManager sm SET sm.inventoryPermissions = :inventoryPermissions, sm.purchasePermissions = :purchasePermissions WHERE sm.id.store_ID = :storeId AND sm.id.member_ID = :memberId AND sm.nominatorMemberId = :nominatorMemberID")
    void updateStoreManagerPermissions(@Param("memberId") String memberId,
                                       @Param("storeId") String storeId,
                                       @Param("inventoryPermissions") boolean inventoryPermissions,
                                       @Param("purchasePermissions") boolean purchasePermissions,
                                       @Param("nominatorMemberID") String nominatorMemberID);
}


