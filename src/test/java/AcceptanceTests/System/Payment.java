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
import org.mockito.Mockito;
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
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        String userID = "user77";
        userFacade.register(userID,  "username",  "password",  "birthday", "country",  "city", "address",  "name");

        String systemMangerId = "user77";

        //  int systemMangerId1 = userFacade.registerSystemAdmin("david", "password", "birthday","country","city","address","name");
        market.getSystemManagerIds().add(systemMangerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        paymentServicesFacade.addExternalService(licensedDealerNumber, paymentServiceName, url);
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);

        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);

        Map<String, Map<String, Integer>> productList = new HashMap<>();

        // Act and Assert
        assertDoesNotThrow(() -> {paymentServicesFacade.pay(price, cardNumber, cvv, month, year, holderID, systemMangerId, productList);
        });
        int result1 = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(1, result1);
        resetSingletons();

    }

    @Test
    public void invalidPaymentServiceTest_wrongYear() throws Exception {
        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 1997; // Invalid year
        String holderID = "123456789";
        String userID = "user77";

        String systemManagerId = "user77";
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        paymentServicesFacade.addExternalService(licensedDealerNumber, paymentServiceName, url);
      //  market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemManagerId);
        ExternalPaymentService externalPaymentService = null;
        externalPaymentService=  paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);
        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);
        Map<String, Map<String, Integer>> productList = new HashMap<>();
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
        resetSingletons();
    }

    @Test
    public void noAvailableExternalPaymentServiceTest() {

        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 20244;
        String holderID = "123456789";
        String userID = "77";

        String systemManagerId = "77";
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        assertEquals(0, paymentServicesFacade.getAllPaymentServices().size()); // assert there is no AvailableExternalPaymentServiceTest
        Map<String, Map<String, Integer>> productList = new HashMap<>();
//

        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(price, cardNumber, cvv, month, year, holderID, userID, productList);
        });

        assertEquals("There is no available external payment system.\n", exception.getMessage());
        resetSingletons();


    }

    @Test
    public void testPayWithCard_NotEnoughMoney() throws Exception {
        ExternalPaymentService externalPaymentServiceMock = Mockito.mock(ExternalPaymentService.class);

        int price = 100;
        String creditCard = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 2024;
        String holder = "HolderName";
        String id = "UserID";
        Map<String, Map<String, Integer>> productList = new HashMap<>();
        String acquisitionIdCounter = "acquisitionIdCounter";
        String receiptIdCounter = "receiptIdCounter";
        String url = "paypal.com";


        // Mock the HTTP request to return false
// Mock the behavior of the mockHttpRequest method with specific arguments
        Mockito.when(externalPaymentServiceMock.mockHttpRequest(Mockito.anyString(), Mockito.anyString(), Mockito.anyInt())).thenReturn(false);



        // Call the method under test
        Exception exception = assertThrows(Exception.class, () -> {
            externalPaymentServiceMock.payWithCard(price, creditCard, cvv, month, year, holder, id, productList, acquisitionIdCounter, receiptIdCounter);
        });

        // Verify that the exception message is correct
        assertEquals("There is not enough money in the credit card", exception.getMessage());

    }




}
