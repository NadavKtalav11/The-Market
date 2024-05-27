package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class Payment {
    private static BridgeToTests impl;
    private Market market;

    @Mock
    private PaymentServicesFacade paymentServicesFacade;



    @Mock
    private UserFacade userFacade;

    @Mock
    private StoreFacade storeFacade;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        MockitoAnnotations.openMocks(this);


        this.market = Market.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.userFacade = userFacade.getInstance();

        User mockUser = mock(User.class);

        resetSingletons();

    }
    private void resetSingletons() {
        // Clear the state in Market singleton
        market.getSystemManagerIds().clear();

        // Clear the state in PaymentServicesFacade singleton
        paymentServicesFacade.clearPaymentServices();

        // Add any other necessary resets for singletons here
    }



    @Test
    public void testPayWithExternalPaymentService_Success() throws Exception {
        int price = 100;
        int cardNumber = 12345678;
        int cvv = 123;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        int userID = 77;
        userFacade.register(userID,  "username",  "password",  "birthday", "country",  "city", "address",  "name");

        int systemMangerId = 77;

        //  int systemMangerId1 = userFacade.registerSystemAdmin("david", "password", "birthday","country","city","address","name");
        market.getSystemManagerIds().add(systemMangerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemMangerId);
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);

        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);

        Map<Integer, Map<String, Integer>> productList = new HashMap<>();

        // Act and Assert
        assertDoesNotThrow(() -> {market.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, systemMangerId, productList);
        });
        int result1 = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(1, result1);

    }

    @Test
    public void invalidPaymentServiceTest() throws Exception {
        int price = 100;
        int cardNumber = 12345678;
        int cvv = 123;
        int month = 12;
        int year = 1997; // Invalid year
        String holderID = "123456789";
        int userID = 77;



        int systemManagerId = 77;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemManagerId);
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);
        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);
        Map<Integer, Map<String, Integer>> productList = new HashMap<>();
//
        //  when(userFacade.isUserLoggedIn(userID)).thenReturn(true);
        // Mocking User and UserFacade
//
        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID, productList);
        });
        int result1 = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result1);
        assertEquals("There is a problem with the provided payment measure or details of the order.\n", exception.getMessage());

    }

    @Test
    public void noAvailableExternalPaymentServiceTest() {

        int price = 100;
        int cardNumber = 12345678;
        int cvv = 123;
        int month = 12;
        int year = 20244;
        String holderID = "123456789";
        int userID = 77;

        int systemManagerId = 77;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        assertEquals(0, paymentServicesFacade.getAllPaymentServices().size()); // assert there is no AvailableExternalPaymentServiceTest
        Map<Integer, Map<String, Integer>> productList = new HashMap<>();
//

        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID, productList);
        });

        assertEquals("There is no available external payment system.\n", exception.getMessage());


    }


}
