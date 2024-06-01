package DomainLayer.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserMemoryRepository<User> implements UserRepository<User> {

    Map<String, User> allUsers;
    Object allUsersLock;

    public UserMemoryRepository(){
        allUsers = new HashMap<>();
        allUsersLock = new Object();
    }

    @Override
    public User get(String i) {
        synchronized (allUsersLock) {
            return allUsers.get(i);
        }
    }

    @Override
    public void add(String userId , User to_add) {
        synchronized (allUsersLock) {
            allUsers.put(userId, to_add);
        }
    }

    @Override
    public void remove(String userId) {
        synchronized (allUsersLock) {

        allUsers.remove(userId);
        }
    }

    @Override
    public List<User> getAll() {
        synchronized (allUsersLock) {
            return new ArrayList<User>(allUsers.values());
        }
    }

    @Override
    public boolean contain(String userId) {
        synchronized (allUsersLock) {
            return allUsers.containsKey(allUsers);
        }
    }

    public void clear(){
        synchronized (allUsersLock) {
            allUsers.clear();
        }
    }
}
