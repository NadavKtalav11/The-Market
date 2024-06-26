package DomainLayer.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStoreOwnerRepository implements  StoreOwnerRepository {
    private Map<String,List<StoreOwner>> memberId_storeOwnersMap = new HashMap<>();
    private final Object storeOwnerLock= new Object();


    @Override
    public StoreOwner get(String storeId, String memberID) {
        synchronized (storeOwnerLock) {
            List<StoreOwner> userOwner = memberId_storeOwnersMap.get(memberID);
            if (userOwner==null){
                return null;
            }
            for (int i = 0; i < userOwner.size(); i++) {
                StoreOwner found = userOwner.get(i);
                if (found.getStore_ID().equals(storeId)) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public void add(StoreOwner storeOwner) {
        synchronized (storeOwnerLock) {
            String memberId = storeOwner.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)==null){
                memberId_storeOwnersMap.put(memberId, new ArrayList<>());
            }
            memberId_storeOwnersMap.get(memberId).add(storeOwner);
        }
    }

    @Override
    public void remove(StoreOwner storeOwner) {
        synchronized (storeOwnerLock) {
            String memberId = storeOwner.getMember_ID();
            if (memberId_storeOwnersMap.get(memberId)!=null){
                memberId_storeOwnersMap.remove(memberId);
            }
        }
    }


    @Override
    public List<String> getAllMemberId() {
        synchronized (storeOwnerLock) {
            return memberId_storeOwnersMap.keySet().stream().toList();
        }
    }

    @Override
    public List<StoreOwner> getAllMemberIdOwners(String memberId) {
        synchronized (storeOwnerLock) {
            return memberId_storeOwnersMap.get(memberId);
        }
    }


}
