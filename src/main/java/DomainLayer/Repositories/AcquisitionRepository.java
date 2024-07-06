package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface AcquisitionRepository extends JpaRepository<Acquisition,String> {

}
