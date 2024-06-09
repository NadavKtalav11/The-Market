package DomainLayer.PaymentServices;


import Util.PaymentDTO;
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
        String licensedDealerNumber = "1";
        String paymentServiceName = "TestService";
        String url = "http://testservice.com";

        boolean isAdded = paymentServicesFacade.addExternalService(licensedDealerNumber, paymentServiceName, url);
        assertTrue(isAdded);
    }

    @Test
    public void testPay() throws Exception {
        int price = 100;
        String creditCard = "123456789";
        int cvv = 123;
        int month = 12;
        int year = 2023;
        String holderID = "holderID";
        String userId = "user1";
        Map<String, Map<String, Integer>> productList = new HashMap<>();
        productList.put("store1", new HashMap<>());

        paymentServicesFacade.addExternalService("1", "TestService", "http://testservice.com");
        Map<String,String> acquisitionAndExternalService = null;

        acquisitionAndExternalService= paymentServicesFacade.pay(price, new PaymentDTO(holderID, creditCard, cvv, month, year), userId, productList);
        assertNotNull(acquisitionAndExternalService);
    }

//
}
