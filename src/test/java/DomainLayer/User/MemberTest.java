package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private Member member;
    private final int memberId = 1;
    private final String username = "testUser";
    private final String password = "testPass";
    private final String birthday = "01-01-2000";
    private final String address = "123 Test St";

    @BeforeEach
    public void setUp() {
        member = new Member(memberId, username, password, birthday, address);
    }

    @Test
    public void testMemberInitialization() {
        assertEquals(memberId, member.getMemberID());
        assertEquals(username, member.getUsername());
        assertEquals(password, member.getPassword());
        assertEquals(birthday, member.getBirthday());
        assertEquals(address, member.getAddress());
        assertFalse(member.isLogin());
    }

    @Test
    public void testLogout() {
        member.Logout(null); // No need to pass user since it's not used in Logout method
        assertFalse(member.isLogin());
    }

    @Test
    public void testExit() {
        // Exit method should simply call Logout, so we just need to verify if isLogin becomes false
        member.exitMarketSystem(null); // No need to pass user since it's not used in Exit method
        assertFalse(member.isLogin());
    }


}
