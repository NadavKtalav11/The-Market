package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RemoveExternalPaymentServices {
    private static BridgeToTests impl;
    private Market market;



    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.market = Market.getInstance();
        market.getSystemManagerIds().add("77");

    }


    @Test
    public void testRemoveExternalPaymentServiceSuccess() throws Exception {
        market.addExternalPaymentService(new PaymentServiceDTO("12345", "PayPal", "http://paypal.com"), "77");
        market.addExternalPaymentService(new PaymentServiceDTO("67890", "Stripe", "http://stripe.com"),"77");

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.removeExternalPaymentService("12345", "77");
        });
    }

    @Test
    public void testRemoveExternalPaymentServiceFailureNotSystemManager() throws Exception {
        // Arrange
        market.addExternalPaymentService(new PaymentServiceDTO("12345", "PayPal", "http://paypal.com"),"77");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalPaymentService("12345", "2"); // 2 is not a system manager ID
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.SystemManagerPaymentAuthorizationRemove.toString(), exception.getMessage());
    }

    @Test
    public void testRemoveExternalPaymentServiceFailureOnlyOneService() throws Exception {
        // Arrange
        market.addExternalPaymentService(new PaymentServiceDTO("12345", "PayPal", "http://paypal.com"),"77");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalPaymentService("12345", "77");
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.OnlyPaymentService.toString(), exception.getMessage());
    }

}
