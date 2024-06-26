package DomainLayer.Role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryStoreManagerRepository implements StoreManagerRepository{

    private Map<String,List<StoreManager>> memberId_storeManagerMap= new HashMap<>();
    private final Object storeManagerLock= new Object();


    @Override
    public StoreManager get(String storeId, String memberId) {
        synchronized (storeManagerLock) {
            List<StoreManager> userManager = memberId_storeManagerMap.get(memberId);
            if (userManager==null){
                return null;
            }
            for (int i = 0; i < userManager.size(); i++) {
                StoreManager found = userManager.get(i);
                if (found.getStore_ID().equals(storeId)) {
                    return found;
                }
            }
        }
        return null;
    }

    @Override
    public void add(StoreManager storeManager) {
        String memberId;
        synchronized (storeManagerLock) {
            memberId = storeManager.getMember_ID();
            //if (memberId_storeManagerMap.get(memberId)==null){
            memberId_storeManagerMap.put(memberId, new ArrayList<>());
            memberId_storeManagerMap.get(memberId).add(storeManager);
        }
    }

    @Override
    public void remove(StoreManager storeManager) {
        synchronized (storeManagerLock) {
            String memberId = storeManager.getMember_ID();
            if (memberId_storeManagerMap.get(memberId)!=null){
                memberId_storeManagerMap.remove(memberId);
            }
        }
    }

    @Override
    public List<String> getAllMemberId() {
        synchronized (storeManagerLock) {
            return memberId_storeManagerMap.keySet().stream().toList();
        }
    }

    @Override
    public List<StoreManager> getAllMemberIdOwners(String memberId) {
        synchronized (storeManagerLock) {
            return memberId_storeManagerMap.get(memberId);
        }
    }
}
