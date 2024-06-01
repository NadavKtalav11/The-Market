package DomainLayer.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemberMemoryRepository implements MemberRepository {

    Map<String, Member> allMembers;
    Object allMembersLock;

    public MemberMemoryRepository(){
        allMembers = new HashMap<>();
        allMembersLock = new Object();
    }

    @Override
    public Member get(String id) {
        synchronized (allMembersLock) {
            return allMembers.get(id);
        }
    }

    @Override
    public void add(String userId , Member to_add) {
        synchronized (allMembersLock) {
            allMembers.put(userId, to_add);
        }
    }

    @Override
    public void remove(String userId) {
        synchronized (allMembersLock) {

            allMembers.remove(userId);
        }
    }

    @Override
    public List<Member> getAll() {
        synchronized (allMembersLock) {
            return new ArrayList<Member>(allMembers.values());
        }
    }

    @Override
    public boolean contain(String memberId) {
        synchronized (allMembersLock) {
            return allMembers.containsKey(memberId);
        }
    }

    public void clear(){
        synchronized (allMembersLock) {
            allMembers.clear();
        }
    }

    @Override
    public Member getByUserName(String member) {
        synchronized (allMembersLock) {
            for (Member curr_member : allMembers.values()) {
                if (curr_member.getUsername().equals(member)) {
                    return curr_member;
                }
            }
        }
        return null;
    }
}

