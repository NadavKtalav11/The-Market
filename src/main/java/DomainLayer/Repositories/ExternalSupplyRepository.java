package DomainLayer.Repositories;

import DomainLayer.SupplyServices.ExternalSupplyService;
import Util.ShippingDTO;
import jakarta.transaction.Transactional;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface ExternalSupplyRepository extends JpaRepository<ExternalSupplyService,String> {
    @Query("SELECT s FROM ShippingDTO s WHERE s.memberId = :memberId")
    public List<ShippingDTO> getUserHistory(String memberId);

    @Query("SELECT s FROM ShippingDTO s")
    public List<ShippingDTO> getSystemHistory();

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO ShippingDTO (shippingDTO) VALUES (:shippingDTO)", nativeQuery = true)
    public void addShippingDTO(ShippingDTO shippingDTO);
}
