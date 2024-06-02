package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Login {

    private static BridgeToTests impl;



    @BeforeAll
    public static void setUp() {

        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.enterMarketSystem();
        impl.enterMarketSystem(); 
        impl.register("0", new UserDTO("user1","12/12/00","Israel", "BeerSheva", "bialik","noa"), "0VnDExW3T9");
        impl.register("1", new UserDTO("user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "QtzxeceVM0");
        impl.register("2", new UserDTO("user3", "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "KjUJqvJBls");
    }

    @AfterEach
    public void shutDown() {
        impl.logout("0");
        impl.logout("1");
        impl.logout("2");
    }

    @Test
    public void successfulLoginTest() {
        assertTrue(impl.login("0", "user1", "0VnDExW3T9").isSuccess());
        assertTrue(impl.login("1", "user2", "QtzxeceVM0").isSuccess());
        assertTrue(impl.login("2", "user3", "KjUJqvJBls").isSuccess());
    }

    @Test
    public void incorrectUsernameTest() {
        assertFalse(impl.login("0", "user7", "0VnDExW3T9").isSuccess());
        assertFalse(impl.login("1", "user8", "QtzxeceVM0").isSuccess());
        assertFalse(impl.login("2", "user9", "KjUJqvJBls").isSuccess());
    }

    @Test
    public void incorrectPasswordTest() {
        assertFalse(impl.login("0", "user1", "123").isSuccess());
        assertFalse(impl.login("1", "user2", "456").isSuccess());
        assertFalse(impl.login("2", "user3", "789").isSuccess());
    }

    @Test
    public void alreadyLoggedInTest() {
        impl.login("0", "user1", "0VnDExW3T9").isSuccess();
        impl.login("1", "user2", "QtzxeceVM0").isSuccess();
        impl.login("2", "user3", "KjUJqvJBls").isSuccess();

        assertFalse(impl.login("0", "user1", "0VnDExW3T9").isSuccess());
        assertFalse(impl.login("1", "user2", "QtzxeceVM0").isSuccess());
        assertFalse(impl.login("2", "user3", "KjUJqvJBls").isSuccess());
    }
}
