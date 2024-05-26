package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class EnteringMarket {

    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need


    }

    @Test
    public void successfulEnterTest() {
        impl.enterMarketSystem();
        assertTrue(impl.enterMarketSystem().isSuccess());
        assertTrue(impl.enterMarketSystem().isSuccess());
        assertTrue(impl.enterMarketSystem().isSuccess());

    }
}
