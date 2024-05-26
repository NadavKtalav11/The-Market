package DomainLayer.PaymentServices;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class PaymentServicesFacadeTest {

    private PaymentServicesFacade paymentServicesFacade;
    private ExternalPaymentService mockExternalPaymentService;
    private Acquisition mockAcquisition;
    private Receipt mockReceipt;

    @BeforeEach
    public void setUp() throws Exception {
        resetSingleton();
        paymentServicesFacade = PaymentServicesFacade.getInstance();
        mockExternalPaymentService = Mockito.mock(ExternalPaymentService.class);
        mockAcquisition = Mockito.mock(Acquisition.class);
        mockReceipt = Mockito.mock(Receipt.class);
    }
    // Reflectively reset the singleton instance for testing purposes
    private void resetSingleton() throws Exception {
        Field instance = PaymentServicesFacade.class.getDeclaredField("paymentServicesFacadeInstance");
        instance.setAccessible(true);
        instance.set(null, null);
    }

    @Test
    public void testSingletonInstance() {
        PaymentServicesFacade instance1 = PaymentServicesFacade.getInstance();
        PaymentServicesFacade instance2 = PaymentServicesFacade.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    public void testAddExternalService() {
        int licensedDealerNumber = 1;
        String paymentServiceName = "TestService";
        String url = "http://testservice.com";

        boolean isAdded = paymentServicesFacade.addExternalService(licensedDealerNumber, paymentServiceName, url);
        assertTrue(isAdded);
    }

    @Test
    public void testPay() {
        int price = 100;
        int creditCard = 123456789;
        int cvv = 123;
        int month = 12;
        int year = 2023;
        String holderID = "holderID";
        int userId = 1;
        Map<Integer, Map<String, Integer>> productList = new HashMap<>();
        productList.put(1, new HashMap<>());

        paymentServicesFacade.addExternalService(1, "TestService", "http://testservice.com");
        Map<Integer,Integer> acquisitionAndExternalService = null;

        acquisitionAndExternalService= paymentServicesFacade.pay(price, creditCard, cvv, month, year, holderID, userId, productList);
        assertNotNull(acquisitionAndExternalService);
    }

    @Test
    public void testGetStorePurchaseInfo() {
        Map<Integer, Receipt> storeIdAndReceipt = new HashMap<>();
        storeIdAndReceipt.put(1, mockReceipt);

        when(mockAcquisition.getStoreIdAndReceipt()).thenReturn(storeIdAndReceipt);
        when(mockReceipt.getStoreId()).thenReturn(1);

        Map<Integer, Acquisition> acquisitionMap = new HashMap<>();
        acquisitionMap.put(1, mockAcquisition);

        //todo remove this comment before push
        //when(mockExternalPaymentService.getIdAndAcquisition()).thenReturn(acquisitionMap);
        paymentServicesFacade.getAllPaymentServices().put(1, mockExternalPaymentService);

        Map<Integer, Integer> result = paymentServicesFacade.getStorePurchaseInfo();

        assertNotNull(result);
        assertEquals(1, result.get(1).intValue());
    }

    @Test
    public void testGetStoreReceiptsAndTotalAmount() {
        int storeId = 1;
        int receiptId = 1;
        int totalPrice = 100;

        Map<Integer, Receipt> storeIdAndReceipt = new HashMap<>();
        storeIdAndReceipt.put(storeId, mockReceipt);

        when(mockAcquisition.getStoreIdAndReceipt()).thenReturn(storeIdAndReceipt);
        when(mockAcquisition.getReceiptIdByStoreId(storeId)).thenReturn(receiptId);
        when(mockAcquisition.getTotalPriceOfStoreInAcquisition(storeId)).thenReturn(totalPrice);

        Map<Integer, Acquisition> acquisitionMap = new HashMap<>();
        acquisitionMap.put(1, mockAcquisition);

        //todo remove this comment before push
        // when(mockExternalPaymentService.getIdAndAcquisition()).thenReturn(acquisitionMap);
        paymentServicesFacade.getAllPaymentServices().put(1, mockExternalPaymentService);

        Map<Integer, Integer> result = paymentServicesFacade.getStoreReceiptsAndTotalAmount(storeId);

        assertNotNull(result);
        assertEquals(totalPrice, result.get(receiptId).intValue());
    }
}
