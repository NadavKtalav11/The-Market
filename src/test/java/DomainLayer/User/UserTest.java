package DomainLayer.User;

import DomainLayer.Store.Store;
import DomainLayer.User.*;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    private User user;
    private Member member;
    private Cart mockCart;
    private State mockState;
    private Store storeMock;
    private int userID = 1;

    @BeforeEach
    public void setUp() {
        user = new User(1);
        member = mock(Member.class);

        // Create the mocks
        mockCart = Mockito.mock(Cart.class);
        storeMock = Mockito.mock(Store.class);

        // Initialize the user and set the user's cart
        user.setCart(mockCart);
    }

    @Test
    public void testUserInitialization() {
        assertEquals(userID, user.getUserID());
        assertTrue(user.getState() instanceof Guest);
        assertNotNull(user.getCart());
    }

    @Test
    public void testSetState() {
        user.setState(mockState);
        assertEquals(mockState, user.getState());
    }

    @Test
    public void testLogout() {
        user.setState(member);
        user.Logout();
        assertFalse(user.isMember());
    }

    @Test
    public void testAddToCart() {
        User user2 = new User(2);
        user2.addToCart("Product1", 2, 1, 100);
        assertFalse(user2.getCart().isCartEmpty());
        assertTrue(user2.checkIfProductInUserCart("Product1", 1));
    }

    @Test
    public void testModifyProductInCart() {
        User user2 = new User(2);
        user2.addToCart("Product1", 2, 1, 100);
        user2.modifyProductInCart("Product1", 3, 1, 150);
        assertEquals(1, user2.getCartProductsByStore(1).size());
        assertEquals(3, user2.getCartProductsByStore(1).get("Product1").get(0));
    }

    @Test
    void testLogin() throws Exception {
        user.Login(member);
        assertTrue(user.isLoggedIn());
        assertEquals(member, user.getState());
    }

    @Test
    public void testIsLoggedIn() {
        assertFalse(user.isLoggedIn());
        user.setState(new Member(1,new UserDTO("username1", "1.2.2020", "israel" , "bash", "bialik", "noa"), "password"));
        assertTrue(user.isLoggedIn());
    }

    @Test
    public void testRemoveItemFromUserCart() {
        String productName = "Product1";
        int storeId = 100;

        // Set up the mock behavior for the store
        when(storeMock.getStoreID()).thenReturn(storeId);

        // Call the method to be tested
        user.removeItemFromUserCart(productName, storeId);

        // Verify that the removeItemFromCart method was called on the mock cart with the correct parameters
        verify(mockCart).removeItemFromCart(productName, storeId);
    }

    @Test
    public void testAddReceipt() {
        //already tests in member and guest
    }

    @Test
    public void testGetCartProductsByStore() {
        User user2 = new User(2);
        user2.addToCart("Product1", 2, 1, 100);
        user2.addToCart("Product2", 1, 1, 50);
        assertEquals(2, user2.getCartProductsByStore(1).size());
    }

    @Test
    //todo check this after Tomer
    public void testGetCartTotalPriceBeforeDiscount() {
        User user2 = new User(2);
        user2.addToCart("Product1", 2, 1, 100);
        user2.addToCart("Product2", 1, 1, 50);

        assertEquals(150, user2.getCartTotalPriceBeforeDiscount());
    }

    @Test
    public void testIsCartEmpty() {
        User user2 = new User(2);
        assertTrue(user2.isCartEmpty());
        user2.addToCart("Product1", 2, 1, 100);
        assertFalse(user2.isCartEmpty());
    }
}
