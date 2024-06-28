package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.User.UserFacade;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;


public class  SystemStartup {
    private static BridgeToTests impl;
    private Market market;
    private UserFacade userFacade;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.userFacade = UserFacade.getInstance();
        this.market = new Market(userFacade);

    }

    @Test
    public void successfulInitTest() throws Exception {
        assertFalse(market.isInitialized());
        assertEquals(0, market.getSystemManagerIds().size());


        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://example.com";
        String licensedDealerNumber1 = "67890";
        String supplyServiceName = "SupplyService";
        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

        // Act
        market.init(new PaymentServiceDTO(licensedDealerNumber,
                paymentServiceName, url), new SupplyServiceDTO(licensedDealerNumber1, supplyServiceName, countries, cities));

        // Assuming there's a method isInitialized() that returns whether the system is initialized
        assertTrue(market.isInitialized());
        assertEquals(1, market.getSystemManagerIds().size());
        String memberID = market.getSystemManagerIds().iterator().next();

        // assert the system manager details match the details in the config file
        assertEquals("u1", userFacade.getMembers().get(memberID).getUsername());
        assertEquals("19/09/1996", userFacade.getMembers().get(memberID).getBirthday());
        assertEquals("Israel", userFacade.getMembers().get(memberID).getCountry());
        assertEquals("Ashdod", userFacade.getMembers().get(memberID).getCity());
        assertEquals("Elul", userFacade.getMembers().get(memberID).getAddress());
        assertEquals("David Volodarsky", userFacade.getMembers().get(memberID).getName());

    }

    @Test
    public void testInitFailureNoSupplyService() {
        assertFalse(market.isInitialized());

        // Arrange

        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://example.com";
        String licensedDealerNumber1 = "67890";
        String supplyServiceName = null; // No supply service provided
        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init( new PaymentServiceDTO(licensedDealerNumber,
                    paymentServiceName, url), new SupplyServiceDTO( licensedDealerNumber1, supplyServiceName, countries, cities));
        });


        // Optionally check the exception message
        assertEquals(ExceptionsEnum.InvalidSupplyServiceDetails.toString(), exception.getMessage());
        assertFalse(market.isInitialized());

    }
    @Test
    public void noPaymentServiceTest() {
        assertFalse(market.isInitialized());

        // Arrange
        String userName = "manager";
        String password = "password123";
        String birthday = "1990-01-01";
        String country = "USA";
        String city = "New York";
        String address = "123 Main St";
        String name = "Manager Name";
        String licensedDealerNumber = "12345";
        String paymentServiceName = null;
        String url = "http://example.com";
        String licensedDealerNumber1 = "67890";
        String supplyServiceName = "serviceService"; // No supply service provided
        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

//        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init(new PaymentServiceDTO( licensedDealerNumber,
                    paymentServiceName, url) ,new SupplyServiceDTO( licensedDealerNumber1, supplyServiceName, countries, cities));
        });
////
////
////        // Optionally check the exception message
        assertEquals(ExceptionsEnum.InvalidPaymentServiceDetails.toString(), exception.getMessage());
        assertFalse(market.isInitialized());
    }
}
