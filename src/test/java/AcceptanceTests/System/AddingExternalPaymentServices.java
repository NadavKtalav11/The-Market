package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AddingExternalPaymentServices {
    private static BridgeToTests impl;
    private Market market;



    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.market = Market.getInstance();

    }

    @Test
    public void testAddExternalPaymentServiceSuccess() {
        // Arrange
        int systemManagerId = 77;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemManagerId);
        });
    }


    @Test
    public void testAddExternalPaymentServiceFailureNotSystemManager() {
        // Arrange
        int systemManagerId = 1;
        int nonManagerId = 2;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String paymentServiceName = "PayPal";
        String url = "http://paypal.com";

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, nonManagerId);
        });

        // Optionally check the exception message
        assertEquals("Only system manager is allowed to add new external payment service", exception.getMessage());
    }

    @Test
    public void testAddExternalPaymentServiceFailureInvalidDetails() {
        // Arrange
        int systemManagerId = 1;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = -1; // Invalid dealer number
        String paymentServiceName = null; // Invalid payment service name
        String url = null; // Invalid URL

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService(licensedDealerNumber, paymentServiceName, url, systemManagerId);
        });

        // Optionally check the exception message
        assertEquals("The system has not been able to add the payment service due to invalid details", exception.getMessage());
    }

}
