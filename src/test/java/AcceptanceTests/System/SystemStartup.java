package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;


public class SystemStartup {
    private static BridgeToTests impl;
    private Market market;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.market = Market.getInstance();

    }

    @Test
    public void successfulInitTest() throws Exception {
        assertFalse(market.isInitialized());

        String userName = "manager";
        String password = "password123";
        String birthday = "01/01/1990";
        String country = "USA";
        String city = "New York";
        String address = "123 Main St";
        String name = "Manager Name";
        String licensedDealerNumber = "12345";
        String paymentServiceName = "PayPal";
        String url = "http://example.com";
        String licensedDealerNumber1 = "67890";
        String supplyServiceName = "SupplyService";
        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

        // Act
        market.init(new UserDTO(null, userName, birthday, country, city, address, name), password, new PaymentServiceDTO(licensedDealerNumber,
                paymentServiceName, url), new SupplyServiceDTO(licensedDealerNumber1, supplyServiceName, countries, cities));

        // Assuming there's a method isInitialized() that returns whether the system is initialized
        assertTrue(market.isInitialized());

    }

    @Test
    public void testInitFailureNoSupplyService() {
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
        String paymentServiceName = "PayPal";
        String url = "http://example.com";
        String licensedDealerNumber1 = "67890";
        String supplyServiceName = null; // No supply service provided
        HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init(new UserDTO(null , userName, birthday, country, city, address, name), password, new PaymentServiceDTO(licensedDealerNumber,
                    paymentServiceName, url), new SupplyServiceDTO( licensedDealerNumber1, supplyServiceName, countries, cities));
        });


        // Optionally check the exception message
        assertEquals("The system has not been able to be launched since there is a problem with the supply service details", exception.getMessage());
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

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.init(new UserDTO(null , userName, birthday, country, city, address, name), password,new PaymentServiceDTO( licensedDealerNumber,
                    paymentServiceName, url) ,new SupplyServiceDTO( licensedDealerNumber1, supplyServiceName, countries, cities));
        });


        // Optionally check the exception message
        assertEquals("The system has not been able to be launched since there is a problem with the payment service details", exception.getMessage());
        assertFalse(market.isInitialized());
    }
}
