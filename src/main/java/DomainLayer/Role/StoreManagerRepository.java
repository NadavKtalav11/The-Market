package DomainLayer.Role;

import java.util.List;

public interface StoreManagerRepository {

    public StoreManager get(String storeId, String memberId);
    public void add(StoreManager to_add);
    public List<String> getAllMemberId();
    public List<StoreManager> getAllMemberIdOwners(String memberId);

}

