package IntegrationTests.PaymentServices;

import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import Util.PaymentDTO;
import Util.PaymentServiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentServicesFacadeTest {

    private PaymentServicesFacade paymentServicesFacade;
    private PaymentDTO paymentDTO;

    @BeforeEach
    public void setUp() {
        paymentServicesFacade = PaymentServicesFacade.getInstance().newForTest();
        paymentDTO = new PaymentDTO("130", "david", "USD","98767576576", 986, 6,2030);

    }

    @Test
    public void testAddExternalServiceWithParams() {
        boolean added = paymentServicesFacade.addExternalService( "http://test.com");
        assertTrue(added);

        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL("http://test.com");
        assertNotNull(service);
        assertEquals("http://test.com", service.getUrl());
    }

    @Test
    public void testAddExternalServiceWithDTO() {
      //  PaymentServiceDTO paymentServiceDTO = new PaymentServiceDTO("123", "TestService", "http://test.com");

        boolean added = paymentServicesFacade.addExternalService("http://test.com");
        assertTrue(added);

        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL("http://test.com");
        assertNotNull(service);
        assertEquals("http://test.com", service.getUrl());
    }

    @Test
    public void testRemoveExternalService() {
        String url = "http://test.com";
        paymentServicesFacade.addExternalService( url);
        paymentServicesFacade.removeExternalService(url);

        ExternalPaymentService service = paymentServicesFacade.getPaymentServiceByURL(url);
        assertNull(service);
    }

    @Test
    public void testPaySuccess() throws Exception {
        paymentServicesFacade.addExternalService("http://test.com");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        String result = paymentServicesFacade.pay(100, paymentDTO, "userId", productList);

        assertNotNull(result);
    }

    @Test
    public void testGetAllPaymentServices() {
        paymentServicesFacade.addExternalService( "http://test.com");

        Map<String, ExternalPaymentService> allServices = paymentServicesFacade.getAllPaymentServices();
        assertEquals(1, allServices.size());
        assertTrue(allServices.containsKey("123"));
    }

    @Test
    public void testGetPaymentServiceDTOById() {
        paymentServicesFacade.addExternalService( "http://test.com");

//        PaymentServiceDTO dto = paymentServicesFacade.getPaymentServiceDTOById("123");
//        assertNotNull(dto);
//        assertEquals("123", dto.getLicensedDealerNumber());
    }

    @Test
    public void testGetStorePurchaseInfo() {
        paymentServicesFacade.addExternalService("http://test.com");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        try {
            paymentServicesFacade.pay(100, paymentDTO, "userId", productList);
        } catch (Exception e) {
            fail("Payment failed");
        }

        Map<String, Integer> storePurchaseInfo = paymentServicesFacade.getStorePurchaseInfo();
        assertNotNull(storePurchaseInfo);
        assertEquals(1, storePurchaseInfo.size());
        assertTrue(storePurchaseInfo.containsKey("store1"));
        assertEquals(1, storePurchaseInfo.get("store1"));
    }

    @Test
    public void testGetStoreReceiptsAndTotalAmount() {
        paymentServicesFacade.addExternalService( "http://test.com");

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
        Map<String, List<Integer>> storeProducts = new HashMap<>();
        storeProducts.put("product1", Arrays.asList(2, 100));
        productList.put("store1", storeProducts);

        try {
            paymentServicesFacade.pay(100, paymentDTO, "userId", productList);
        } catch (Exception e) {
            fail("Payment failed");
        }

        Map<String, Integer> storeReceiptsAndTotalAmount = paymentServicesFacade.getStoreReceiptsAndTotalAmount("store1");
        assertNotNull(storeReceiptsAndTotalAmount);
        assertEquals(1, storeReceiptsAndTotalAmount.size());
    }

    @Test
    public void testClearPaymentServices() {
        paymentServicesFacade.addExternalService( "http://test.com");
        paymentServicesFacade.clearPaymentServices();

        assertTrue(paymentServicesFacade.getAllPaymentServices().isEmpty());
    }
}
