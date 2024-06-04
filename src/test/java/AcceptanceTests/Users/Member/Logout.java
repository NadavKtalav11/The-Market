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
    static String  userId0;
    static String  userId1;
    static String  userId2;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        userId0 = impl.enterMarketSystem().getData();
        impl.register(userId0, "user1", "12/12/00","Israel", "BeerSheva", "bialik","noa", "0VnDExW3T9");
        userId1 = impl.enterMarketSystem().getData();

        impl.register(userId1, "user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa", "QtzxeceVM0");
        userId2 = impl.enterMarketSystem().getData();
        impl.register(userId2, "user3",  "12/12/00", "Israel", "BeerSheva", "bialik","noa", "KjUJqvJBls");

        impl.login(userId0, "user1", "0VnDExW3T9");


    }



    @Test
    public void successfulLogoutTest() {
        assertTrue(impl.logout(userId0).isSuccess());


    }

    @Test
    public void alreadyLoggedOutTest() {

        assertFalse(impl.logout(userId1).isSuccess());
    }

}
