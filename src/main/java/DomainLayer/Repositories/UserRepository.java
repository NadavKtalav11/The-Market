package DomainLayer.Repositories;

import DomainLayer.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserRepository extends JpaRepository<User, String> {
    // Additional methods specific to User can be added here
}