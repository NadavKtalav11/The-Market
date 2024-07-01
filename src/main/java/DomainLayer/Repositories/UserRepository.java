package DomainLayer.Repositories;

import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    public User get(String i);
    //public void add(String userId, User to_add);
    public void remove(String to_remove);
    //public List<User> getAll();
    public boolean contain(String userId);
    public void clear();

}
