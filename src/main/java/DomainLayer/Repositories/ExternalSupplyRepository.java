package DomainLayer.Repositories;

import DomainLayer.SupplyServices.ExternalSupplyService;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface ExternalSupplyRepository extends JpaRepository<ExternalSupplyService,String> {
}
