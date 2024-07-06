package DomainLayer.Repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("memory")
public class ExternalPaymentMemoryRepository {
}
