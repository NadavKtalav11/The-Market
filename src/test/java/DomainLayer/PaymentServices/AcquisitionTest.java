package DomainLayer.PaymentServices;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AcquisitionTest {

    private Acquisition acquisition;

    @Mock
    private PaymentDTO mockPayment;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mock PaymentDTO values
        when(mockPayment.getHolderId()).thenReturn("123456789");
        when(mockPayment.getCreditCardNumber()).thenReturn("1234567812345678");
        when(mockPayment.getCvv()).thenReturn(123);
        when(mockPayment.getMonth()).thenReturn(12);
        when(mockPayment.getYear()).thenReturn(2024);

        // Mock product list
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        productList.put("store1", new HashMap<>());
        productList.get("store1").put("Product1", Arrays.asList(2, 100)); // Quantity, Price
        productList.get("store1").put("Product2", Arrays.asList(1, 50));

        // Initialize Acquisition object
        acquisition = new Acquisition("acq-1", "user-1", 150, mockPayment, productList, "receipt-1");
    }

    @Test
    public void testGetUserId() {
        assertEquals("user-1", acquisition.getUserId());
    }

    @Test
    public void testGetTotalPrice() {
        assertEquals(150, acquisition.getTotalPrice());
    }

    @Test
    public void testGetTotalPriceOfStoreInAcquisition() {
        int totalPriceStore1 = acquisition.getTotalPriceOfStoreInAcquisition("store1");
        assertEquals(150, totalPriceStore1); // Expected total: 2 * 100 + 1 * 50 = 250
    }

    @Test
    public void testGetReceiptIdByStoreId() {
        String receiptIdStore1 = acquisition.getReceiptIdByStoreId("store1");
        assertEquals("receipt-1", receiptIdStore1);
    }

    @Test
    public void testGetReceiptIdAndStoreIdMap() {
        Map<String, String> receiptIdMap = acquisition.getReceiptIdAndStoreIdMap();
        assertEquals(1, receiptIdMap.size());
        assertEquals("store1", receiptIdMap.get("receipt-1"));
    }
}
