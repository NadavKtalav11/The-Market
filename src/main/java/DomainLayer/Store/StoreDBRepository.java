package DomainLayer.Store;

import DomainLayer.Role.StoreOwnerRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("db")
public class StoreDBRepository extends StoreOwnerRepository {
}
