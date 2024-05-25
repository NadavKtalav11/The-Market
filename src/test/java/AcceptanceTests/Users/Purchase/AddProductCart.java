package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AddProductCart {

    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

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
