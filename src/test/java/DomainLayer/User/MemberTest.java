package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MemberTest {

    private Member member;
    private final int memberId = 1;
    private final String username = "testUser";
    private final String password = "testPass";
    private final String birthday = "01-01-2000";
    private final String country = "testCountry";
    private final String city = "testCity";
    private final String address = "123 Test St";
    private final String name = "testName";

    @BeforeEach
    public void setUp() {
        member = new Member(memberId, username, password,birthday,country,city,address, name);
    }


    @Test
    public void testLoginThrowsException() {
        // Assert that Login throws an exception when called
        assertThrows(Exception.class, member::Login);
    }

    @Test
    public void testLogout() {
        // Assert that logout does not throw any exception
        assertDoesNotThrow(member::Logout);
    }

    @Test
    public void testExitMarketSystem() {
        // Assert that exitMarketSystem does not throw any exception
        assertDoesNotThrow(member::exitMarketSystem);
    }

    @Test
    public void testIsMember() {
        // Assert that isMember returns true for a member
        assertTrue(member.isMember());
    }

    @Test
    public void testAddReceipt() {
        // Prepare test data
        int receiptId = 123;
        int storeId = 456;
        member.addReceipt(Map.of(receiptId, storeId));

        // Assert that the receipt was added correctly
        assertTrue(member.getReceiptIdsAndStoreId().containsKey(receiptId));
        assertEquals(storeId, member.getReceiptIdsAndStoreId().get(receiptId));
    }
}
