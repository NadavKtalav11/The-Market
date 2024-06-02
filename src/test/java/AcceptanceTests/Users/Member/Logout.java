package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class Logout {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register(0, "user1", "0VnDExW3T9", "12/12/00","Israel", "BeerSheva", "bialik","noa");
        impl.enterMarketSystem();

        impl.register(1, "user2", "QtzxeceVM0", "12/12/00", "Israel", "BeerSheva", "bialik","noa");
        impl.enterMarketSystem();
        impl.register(2, "user3", "KjUJqvJBls", "12/12/00", "Israel", "BeerSheva", "bialik","noa");

        impl.login(0, "user1", "0VnDExW3T9");


    }



    @Test
    public void successfulLogoutTest() {
        assertTrue(impl.logout(0).isSuccess());


    }

    @Test
    public void alreadyLoggedOutTest() {

        assertFalse(impl.logout(1).isSuccess());
    }

}
