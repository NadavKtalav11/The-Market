package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Login {

    private static BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String userID3;




    @BeforeAll
    public static void setUp() {

        impl = new ProxyToTest("Real");
        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        impl.register(userID1, new UserDTO("user1","12/12/00","Israel", "BeerSheva", "bialik","noa"), "0VnDExW3T9");
        impl.register(userID2, new UserDTO("user2", "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "QtzxeceVM0");
        impl.register(userID3, new UserDTO("user3", "12/12/00", "Israel", "BeerSheva", "bialik","noa"), "KjUJqvJBls");
    }

    @AfterEach
    public void shutDown() {
        impl.logout(userID1);
        impl.logout(userID2);
        impl.logout(userID3);
    }

    @Test
    public void successfulLoginTest() {
        Response<String> response1 = impl.login(userID1, "user1", "0VnDExW3T9");
        assertTrue(response1.isSuccess());
        assertEquals("Login successful", response1.getResult());
        Response<String> response2 = impl.login(userID2, "user2", "QtzxeceVM0");
        assertTrue(response2.isSuccess());
        assertEquals("Login successful", response2.getResult());
        Response<String> response3 = impl.login(userID3, "user3", "KjUJqvJBls");
        assertTrue(response3.isSuccess());
        assertEquals("Login successful", response1.getResult());
    }

    @Test
    public void incorrectUsernameTest() {
        // Try logging in with incorrect username
        Response<String> response0 = impl.login(userID1, "wrongUser0", "0VnDExW3T9");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response0.getData());
        Response<String> response1 = impl.login(userID2, "wrongUser0", "QtzxeceVM0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response1.getData());
        Response<String> response2 = impl.login(userID3, "wrongUser0", "KjUJqvJBls");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response2.getData());
    }

    @Test
    public void incorrectPasswordTest() {
        // Try logging in with incorrect password
        Response<String> response0 = impl.login(userID1, "user1", "1");
        assertFalse(response0.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response0.getData());
        Response<String> response1 = impl.login(userID2, "user2", "1");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response1.getData());
        Response<String> response2 = impl.login(userID3, "user3", "1");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.usernameOrPasswordIncorrect.toString(), response2.getData());
    }

    @Test
    public void alreadyLoggedInTest() {
        impl.login(userID1, "user1", "0VnDExW3T9");
        impl.login(userID2, "user2", "QtzxeceVM0");
        impl.login(userID3, "user3", "KjUJqvJBls");

        Response<String> response1 = impl.login(userID1, "user1", "0VnDExW3T9");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response1.getData());
        Response<String> response2 = impl.login(userID2, "user2", "QtzxeceVM0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response2.getData());
        Response<String> response3 = impl.login(userID3, "user3", "KjUJqvJBls");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.userAlreadyLoggedIn.toString(), response3.getData());
    }
}
