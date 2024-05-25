package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AddProductCart {

    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulAdditionTest() {

    }

    @Test
    public void invalidProductNameTest() {

    }

    @Test
    public void outOfStockProductTest() {

    }

    @Test
    public void bigQuantityTest() {

    }

    @Test
    public void negQuantityTest() {

    }

    @Test
    public void purchasePolicyInvalidTest() {

    }
}
