package DomainLayer.Market;

import DomainLayer.Market.Market;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;


public class MarketTest {

    private Market market;

    @BeforeEach
    public void setUp() {
        market = Market.getInstance();
        // You might need to initialize other dependencies here if necessary
    }

    @Test
    public void testInit() throws Exception {
        HashSet<String> countries = new HashSet<>(Arrays.asList("Israel", "Spain"));
        HashSet<String> cities = new HashSet<>(Arrays.asList("Tel Aviv", "Madrid", "Ashdod"));

        market.init("admin", "adminPassword", "birrthday", "Israel", "Bash", "Bialik","noa",1333333, "PaymentService1", "https://paymentservice1.com", 5678, "SupplyService1", countries, cities);
        assertTrue( market.isInitialized() ,"Market should be initialized");

        // Test reinitialization
        market.init("admin", "adminPassword", "birrthday", "Israel", "Bash", "Bialik","noa",1333333, "PaymentService1", "https://paymentservice1.com", 5678, "SupplyService1", countries, cities);
        assertTrue( market.isInitialized(),"Market should remain initialized");
    }

    @Test
    public void testPayWithExternalPaymentService() {
        //todo
        //assertTrue(market.payWithExternalPaymentService(0,0,0,0,"",0), "Payment with external service should be successful");
    }
}