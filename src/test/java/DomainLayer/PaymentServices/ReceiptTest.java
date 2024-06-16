package DomainLayer.PaymentServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReceiptTest {

    private Receipt receipt;
    private final String receiptId = "receipt-123";
    private final String storeId = "store-456";
    private final String userId = "user-789";
    private Map<String, List<Integer>> productList;

    @BeforeEach
    public void setUp() {
        productList = new HashMap<>();
        productList.put("Product1", Arrays.asList(2, 100)); // Quantity, Price
        productList.put("Product2", Arrays.asList(1, 50));

        receipt = new Receipt(receiptId, storeId, userId, productList);
    }

    @Test
    public void testGetTotalPriceOfStoreReceipt() {
        int totalPrice = receipt.getTotalPriceOfStoreReceipt();
        // Expected total: 2 * 100 + 1 * 50 = 250
        assertEquals(150, totalPrice);
    }

    @Test
    public void testGetStoreId() {
        String retrievedStoreId = receipt.getStoreId();
        assertEquals(storeId, retrievedStoreId);
    }

    @Test
    public void testGetReceiptId() {
        String retrievedReceiptId = receipt.getReceiptId();
        assertEquals(receiptId, retrievedReceiptId);
    }
}
