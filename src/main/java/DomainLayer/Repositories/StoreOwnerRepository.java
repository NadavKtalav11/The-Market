package DomainLayer.Repositories;

import DomainLayer.Role.StoreOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StoreOwnerRepository extends JpaRepository<StoreOwner, String> {
    @Query("SELECT so FROM StoreOwner so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId")
    StoreOwner get(String storeId, String memberId);

    @Query("SELECT so FROM StoreOwner so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId AND so.inProposal = false")
    StoreOwner getStoreOwner(String storeId, String memberId);

    //מועמד
    @Query("SELECT so FROM StoreOwner so WHERE so.id.store_ID = :storeId AND so.id.member_ID = :memberId AND so.inProposal = true")
    StoreOwner getStoreOwnerNominator(String storeId, String memberId);

    @Query("SELECT so.id.member_ID FROM StoreOwner so")
    List<String> getAllMemberId();

    @Query("SELECT so FROM StoreOwner so WHERE so.id.member_ID = :memberId AND so.inProposal = false")
    List<StoreOwner> getAllMemberIdOwners(String memberId);

    @Query("SELECT so FROM StoreOwner so WHERE so.id.member_ID = :memberId AND so.inProposal = true")
    List<StoreOwner> getAllMemberIdNominatorsOwners(String memberId);


}
