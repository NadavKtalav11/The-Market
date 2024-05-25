package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Login {

    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {
        impl.register(0, "user1", "0VnDExW3T9", "12/12/00", "BeerSheva");
        impl.register(1, "user2", "QtzxeceVM0", "12/12/00", "BeerSheva");
        impl.register(2, "user3", "KjUJqvJBls", "12/12/00", "BeerSheva");
    }

    @AfterEach
    public void shutDown() {
        impl.logout(0);
        impl.logout(1);
        impl.logout(2);
    }

    @Test
    public void successfulLoginTest() {
        assertTrue(impl.login(0, "user1", "0VnDExW3T9").isSuccess());
        assertTrue(impl.login(1, "user2", "QtzxeceVM0").isSuccess());
        assertTrue(impl.login(2, "user3", "KjUJqvJBls").isSuccess());
    }

    @Test
    public void incorrectUsernameTest() {
        assertFalse(impl.login(0, "user7", "0VnDExW3T9").isSuccess());
        assertFalse(impl.login(1, "user8", "QtzxeceVM0").isSuccess());
        assertFalse(impl.login(2, "user9", "KjUJqvJBls").isSuccess());
    }

    @Test
    public void incorrectPasswordTest() {
        assertFalse(impl.login(0, "user1", "123").isSuccess());
        assertFalse(impl.login(1, "user2", "456").isSuccess());
        assertFalse(impl.login(2, "user3", "789").isSuccess());
    }

    @Test
    public void alreadyLoggedInTest() {
        impl.login(0, "user1", "0VnDExW3T9").isSuccess();
        impl.login(1, "user2", "QtzxeceVM0").isSuccess();
        impl.login(2, "user3", "KjUJqvJBls").isSuccess();

        assertFalse(impl.login(0, "user1", "0VnDExW3T9").isSuccess());
        assertFalse(impl.login(1, "user2", "QtzxeceVM0").isSuccess());
        assertFalse(impl.login(2, "user3", "KjUJqvJBls").isSuccess());
    }
}
