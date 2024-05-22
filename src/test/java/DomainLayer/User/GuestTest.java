package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {

    private Guest guest;
    private User mockUser;
    private final int userId = 1;
    private final String username = "testUser";
    private final String password = "testPass";
    private final String birthday = "01-01-2000";
    private final String address = "123 Test St";

    @BeforeEach
    public void setUp() {
        guest = new Guest();
        //mockUser = new User(userId);
    }


    @Test
    public void testLogin() {
        guest.Login(mockUser, username, password);
        assertTrue(mockUser.getState() instanceof Member);
        // Additional assertions can be added if there are more behaviors to test
    }

    @Test
    public void testLogout() {
        assertDoesNotThrow(() -> guest.Logout(mockUser));
        // No state change or other behavior to verify in Logout method for Guest
    }

    @Test
    public void testExit() {
        assertDoesNotThrow(() -> guest.exitMarketSystem(mockUser));
        // No state change or other behavior to verify in Exit method for Guest
    }
}
