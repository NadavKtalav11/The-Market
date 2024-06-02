package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Registering {

    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.enterMarketSystem();
        impl.enterMarketSystem();
    }

    @Test
    public void successfulRegistrationTest() {
        assertTrue(impl.register(0, new UserDTO("newUser1", "12/12/00", "Israel", "BeerSheva", "bialik", "noa"), "password123").isSuccess());
        assertTrue(impl.login(0, "newUser1", "password123").isSuccess());
        impl.logout(0);
    }

    @Test
    public void registrationWithExistingUsernameTest() {
        impl.register(0, new UserDTO("existingUser", "12/12/00", "Israel", "BeerSheva", "bialik", "noa"), "password123");
        assertFalse(impl.register(1, new UserDTO("existingUser", "12/12/00", "Israel", "BeerSheva", "bialik", "noa"), "differentPassword").isSuccess());
    }

    @Test
    public void registrationWithInvalidDetailsTest() {
        assertFalse(impl.register(0, new UserDTO("", "12/12/00", "Israel", "BeerSheva", "bialik", "noa"), "password123").isSuccess()); // Invalid username
        assertFalse(impl.register(1, new UserDTO("newUser2", "12/12/00", "Israel", "BeerSheva", "bialik", "noa"), "").isSuccess()); // Invalid password
        assertFalse(impl.register(2, new UserDTO("newUser3", "", "Israel", "BeerSheva", "bialik", "noa"), "password123").isSuccess()); // Invalid birthdate
    }
}
