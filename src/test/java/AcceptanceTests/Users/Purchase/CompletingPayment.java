package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CompletingPayment {
    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulPurchaseTest() {

    }


    @Test
    public void paymentFailureTest() {

    }
}
