package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.PaymentServices.HttpClient;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import Util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
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
        CartDTO cartDTO = Mockito.mock(CartDTO.class);
        // Stub the methods as needed
        Mockito.when(cartDTO.getUserID()).thenReturn("77");
        Mockito.when(cartDTO.getCartPrice()).thenReturn(100);
        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        String userID = "77";
        userFacade.register(userID,  new UserDTO(userID, "username", "19/09/1996", "country",  "city", "address",  "name"), "password");

        String systemMangerId = "77";

        //  int systemMangerId1 = userFacade.registerSystemAdmin("david", "password", "birthday","country","city","address","name");
        market.getSystemManagerIds().add(systemMangerId);
        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        paymentServicesFacade.addExternalService(new PaymentServiceDTO(licensedDealerNumber, paymentServiceName, url));
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);

        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);

        Map<Integer, Map<String, Integer>> productList = new HashMap<>();

        // Act and Assert
        assertDoesNotThrow(() -> {paymentServicesFacade.pay(cartDTO.getCartPrice(), new PaymentDTO(holderID, cardNumber, cvv, month, year), cartDTO.getUserID(),cartDTO.getStoreToProducts());
        });
        int result1 = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(1, result1);
        resetSingletons();

    }

    @Test
    public void invalidPaymentServiceTest() throws Exception {
        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 1997; // Invalid year
        String holderID = "123456789";
        String userID = "77";


        String systemManagerId = "77";
        market.getSystemManagerIds().add(systemManagerId);
        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        paymentServicesFacade.addExternalService(new PaymentServiceDTO(licensedDealerNumber, paymentServiceName, url));
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);
        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
//
        //  when(userFacade.isUserLoggedIn(userID)).thenReturn(true);
        // Mocking User and UserFacade
//
        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(new CartDTO(userID,price,productList), new PaymentDTO(holderID, cardNumber, cvv, month, year), userID);
        });
        int result1 = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result1);
        assertEquals( ExceptionsEnum.InvalidCreditCardParameters.toString(), exception.getMessage());

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
        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();
//

        Exception exception = assertThrows(Exception.class, () -> {
            market.payWithExternalPaymentService(new CartDTO(userID,price,productList), new PaymentDTO(holderID, cardNumber, cvv, month, year), userID);
        });

        assertEquals(ExceptionsEnum.noAvailableExternalPaymentService.toString(), exception.getMessage());


    }

    @Test
    public void CreditCardNotPassingTest() throws Exception {

        CartDTO cartDTO = Mockito.mock(CartDTO.class);
        // Stub the methods as needed
        Mockito.when(cartDTO.getUserID()).thenReturn("77");
        Mockito.when(cartDTO.getCartPrice()).thenReturn(100);
        int price = 100;
        String cardNumber = "12345678";
        int cvv = 123;
        int month = 12;
        int year = 2024;
        String holderID = "123456789";
        String userID = "77";
        userFacade.register(userID,  new UserDTO(userID, "username", "19/09/1996", "country",  "city", "address",  "name"), "password");

        String systemMangerId = "77";

        //  int systemMangerId1 = userFacade.registerSystemAdmin("david", "password", "birthday","country","city","address","name");
        market.getSystemManagerIds().add(systemMangerId);
        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";
        HttpClient mockHttpClient = Mockito.mock(HttpClient.class);
        Mockito.when(mockHttpClient.checkCreditCard(Mockito.eq(url), Mockito.any(PaymentDTO.class))).thenReturn(false);

        paymentServicesFacade.addExternalService(new PaymentServiceDTO(licensedDealerNumber, paymentServiceName, url),mockHttpClient);
        ExternalPaymentService externalPaymentService = paymentServicesFacade.getAllPaymentServices().get(licensedDealerNumber);

        int result = externalPaymentService.getIdAndAcquisition().size();
        assertEquals(0, result);

        Map<String, Map<String, List<Integer>>> productList = new HashMap<>();



        Exception exception = assertThrows(Exception.class, () -> {
            paymentServicesFacade.pay(cartDTO.getCartPrice(), new PaymentDTO(holderID, cardNumber, cvv, month, year), cartDTO.getUserID(),cartDTO.getStoreToProducts());
        });

        assertEquals(ExceptionsEnum.CreditCardIssue.toString(), exception.getMessage());


    }


}
