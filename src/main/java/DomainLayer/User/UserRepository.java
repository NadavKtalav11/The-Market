package DomainLayer.User;

import java.util.List;

public interface UserRepository<User> {

    public User get(int i);
    public void add(int userId, User to_add);
    public void remove(int to_remove);
    public List<User> getAll();
    public boolean contain(int userId);
    public void clear();

}
