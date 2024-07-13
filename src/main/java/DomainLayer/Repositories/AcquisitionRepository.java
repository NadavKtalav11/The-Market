package DomainLayer.Repositories;

import DomainLayer.PaymentServices.Acquisition;
import DomainLayer.PaymentServices.ProductDetailReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


@NoRepositoryBean
public interface AcquisitionRepository extends JpaRepository<Acquisition,String> {

    @Query("SELECT p.productDetailReceiptId.productName, p.amount FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId AND p.productDetailReceiptId.storeId = :storeId AND p.acquisition.acquisitionId = :acquisitionId")
    List<Object[]> findProductsAndAmountsByStoreAndReceiptAndAcquisition(@Param("storeId") String storeId, @Param("receiptId") String receiptId, @Param("acquisitionId") String acquisitionId);

    @Query("SELECT p FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId AND p.acquisition.acquisitionId = :acquisitionId")
    List<ProductDetailReceipt> findProductDetailReceiptsByReceiptAndAcquisition(@Param("receiptId") String receiptId, @Param("acquisitionId") String acquisitionId);

    @Query("SELECT SUM(p.price) FROM ProductDetailReceipt p WHERE p.productDetailReceiptId.receiptId = :receiptId AND p.productDetailReceiptId.storeId = :storeId AND p.acquisition.acquisitionId = :acquisitionId")
    int findTotalPriceByStoreAndReceiptAndAcquisition(@Param("storeId") String storeId, @Param("receiptId") String receiptId, @Param("acquisitionId") String acquisitionId);

}
