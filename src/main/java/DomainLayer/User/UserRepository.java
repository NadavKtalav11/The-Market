package DomainLayer.User;

import java.util.List;

public interface UserRepository<User> {

    public User get(String i);
    public void add(String userId, User to_add);
    public void remove(String to_remove);
    public List<User> getAll();
    public boolean contain(String userId);
    public void clear();

}
