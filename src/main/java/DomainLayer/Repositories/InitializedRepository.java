package DomainLayer.Repositories;

import DomainLayer.Market.InitializedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface InitializedRepository extends JpaRepository<InitializedStatus, String> {
}
