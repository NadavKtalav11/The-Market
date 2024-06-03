package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Registering {

    private static BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String userID3;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        userID1 = impl.enterMarketSystem().getResult();
        userID2 = impl.enterMarketSystem().getResult();
        userID3 = impl.enterMarketSystem().getResult();
    }

    @Test
    public void successfulRegistrationTest() {
        assertTrue(impl.register(userID1, "newUser1", "12/12/00", "Israel", "BeerSheva", "bialik", "noa", "password123").isSuccess());
        assertTrue(impl.login(userID1, "newUser1", "password123").isSuccess());
        impl.logout(userID1);
    }

    @Test
    public void registrationWithExistingUsernameTest() {
        impl.register(userID1, "existingUser", "12/12/00", "Israel", "BeerSheva", "bialik", "noa", "password123");
        Response<String> response0 = impl.register(userID2, "existingUser", "12/12/00", "Israel", "BeerSheva", "bialik", "noa", "differentPassword");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response0.getDescription());
    }

    @Test
    public void registrationWithInvalidDetailsTest() {
        Response<String> response0 = impl.register(userID1, "", "12/12/00", "Israel", "BeerSheva", "bialik", "noa", "password123");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.emptyField.toString(), response0.getDescription());

        Response<String> response1 = impl.register(userID2, "newUser2", "12/12/00", "Israel", "BeerSheva", "bialik", "noa", "");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.emptyField.toString(), response1.getDescription());

        Response<String> response2 = impl.register(userID3, "newUser3", "", "Israel", "BeerSheva", "bialik", "noa", "password123");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.emptyField.toString(), response2.getDescription());
    }
}
