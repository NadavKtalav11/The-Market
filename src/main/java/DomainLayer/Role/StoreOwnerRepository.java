package DomainLayer.Role;

import java.util.List;

public interface StoreOwnerRepository {
    public StoreOwner get(String storeId, String memberId);
    public void add(StoreOwner to_add);
    public List<String> getAllMemberId();
    public List<StoreOwner> getAllMemberIdOwners(String memberId);


}
