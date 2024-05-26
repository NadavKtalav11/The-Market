package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class GuestTest {

    private Guest guest;
    private User mockUser;
    private final int userId = 1;
    private final String username = "testUser";
    private final String password = "testPass";
    private final String birthday = "01-01-2000";
    private final String address = "123 Test St";
    private final String country = "Test Country";
    private final String city = "Test City";
    private final String name = "Test Name";

    @BeforeEach
    public void setUp() {
        guest = new Guest();
        mockUser = Mockito.mock(User.class); // Initialize mockUser with a mock object
    }


    @Test
    public void testLogin() throws Exception {
        Member expectedMember = new Member(userId, username, password, birthday, country, city, address, name);
        //todo remove this before.
        //(mockUser.getState()).Login(mockUser, username, password, expectedMember);
        verify(mockUser).setState(expectedMember); // Verify that the setState method was called
        assertTrue(mockUser.getState() instanceof Member); // Additional assertion

    }

    @Test
    public void testLogout() {
        assertDoesNotThrow(() -> guest.Logout());
        // No state change or other behavior to verify in Logout method for Guest
    }

    @Test
    public void testExit() {
        assertDoesNotThrow(() -> guest.exitMarketSystem());
        // No state change or other behavior to verify in Exit method for Guest
    }
}
