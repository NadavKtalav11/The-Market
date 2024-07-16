package DomainLayer.Repositories;

import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User, String> {

    @Query("SELECT u.readyToPay FROM User u WHERE u.userID = :userId")
    boolean getReadyToPay(String userId);
}