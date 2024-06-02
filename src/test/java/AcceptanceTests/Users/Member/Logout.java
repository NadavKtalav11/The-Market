package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.UserDTO;
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
        impl.register(0, new UserDTO("user1", "12/12/00","Israel", "BeerSheva", "bialik","noa"), "0VnDExW3T9");
        impl.enterMarketSystem();

        impl.register(1, new UserDTO("user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "QtzxeceVM0");
        impl.enterMarketSystem();
        impl.register(2, new UserDTO("user3",  "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "KjUJqvJBls");

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
