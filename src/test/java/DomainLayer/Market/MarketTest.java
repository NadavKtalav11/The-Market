package DomainLayer.Market;

import DomainLayer.Market.Market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MarketTest {

    private Market market;

    @BeforeEach
    public void setUp() {
        market = Market.getInstance();
        // You might need to initialize other dependencies here if necessary
    }

    @Test
    public void testInit() {
        market.init("admin", "adminPassword", 1234, "PaymentService1", "https://paymentservice1.com", 5678, "SupplyService1", "123 Street");
        assertTrue( market.isInitialized() ,"Market should be initialized");

        // Test reinitialization
        market.init("admin", "adminPassword", 1234, "PaymentService1", "https://paymentservice1.com", 5678, "SupplyService1", "123 Street");
        assertTrue( market.isInitialized(),"Market should remain initialized");
    }

    @Test
    public void testPayWithExternalPaymentService() {
        assertTrue(market.payWithExternalPaymentService(), "Payment with external service should be successful");
    }
}