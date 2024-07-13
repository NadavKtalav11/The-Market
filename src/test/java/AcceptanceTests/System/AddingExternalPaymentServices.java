package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import PresentationLayer.Application;
import Util.PaymentServiceDTO;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddingExternalPaymentServices {

    @Autowired
    private Market market;



    @BeforeEach
    public void setUp() {

    }

    @Test
    public void testAddExternalPaymentServiceSuccess() {
        // Arrange
        String systemManagerId = "user77";
        market.getSystemManagerIds().add(systemManagerId);
        String url = "http://paypal.com";

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalPaymentService("card",url, systemManagerId);
        });
    }


    @Test
    public void testAddExternalPaymentServiceFailureNotSystemManager() {
        // Arrange
        String systemManagerId = "USER1";
        String nonManagerId = "user2";
        market.getSystemManagerIds().add(systemManagerId);

        String url = "http://paypal.com";

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService("card",url, nonManagerId);
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.SystemManagerPaymentAuthorization.toString(), exception.getMessage());
    }

    @Test
    public void testAddExternalPaymentServiceFailureInvalidDetails() {
        // Arrange
        String systemManagerId = "user1";
        market.getSystemManagerIds().add(systemManagerId);
        String licensedDealerNumber = "-1"; // Invalid dealer number
        String paymentServiceName = null; // Invalid payment service name
        String url = null; // Invalid URL

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalPaymentService("card", url, systemManagerId);
        });

        // Optionally check the exception message
        assertEquals(ExceptionsEnum.InvalidPaymentServiceParameters.toString(), exception.getMessage());
    }

}

