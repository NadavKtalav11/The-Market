package DomainLayer.User;

import DomainLayer.User.*;
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
    private int userID = 1;

    @BeforeEach
    public void setUp() {
        user = new User(1);
        member = mock(Member.class);
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
    //todo do this after Nadav
    public void testLogout() {
        user.setState(mockState);
        user.Logout();
        verify(mockState).Logout();
    }

    @Test
    //todo think what to do
    public void testExit() {
        user.setState(mockState);
        user.exitMarketSystem();
        verify(mockState).exitMarketSystem();
    }

    @Test
    public void testAddToCart() {
        user.addToCart("Product1", 2, 1, 100);
        assertFalse(user.getCart().isCartEmpty());
        assertTrue(user.checkIfProductInUserCart("Product1", 1));
    }

    @Test
    //todo check this after tomer
    public void testModifyProductInCart() {
        user.addToCart("Product1", 2, 1, 100);
        user.modifyProductInCart("Product1", 3, 1, 150);
        assertEquals(1, user.getCartProductsByStore(1).size());
        assertEquals(3, user.getCartProductsByStore(1).get("Product1").get(0));
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
        user.setState(new Member(1,"username1", "password" , "1.2.2020", "israel" , "bash", "bialik", "noa"));
        assertTrue(user.isLoggedIn());
    }

    @Test
    //todo fix this
    public void testRemoveItemFromUserCart() {
        String productName = "Product1";
        int storeId = 100;

        user.removeItemFromUserCart(productName, storeId);
        verify(mockCart).removeItemFromCart(productName, storeId);
    }

    @Test
    public void testAddReceipt() {
        //already tests in member and guest
    }

    @Test
    public void testGetCartProductsByStore() {
        user.addToCart("Product1", 2, 1, 100);
        user.addToCart("Product2", 1, 1, 50);
        assertEquals(2, user.getCartProductsByStore(1).size());
    }

    @Test
    //todo check this after Tomer
    public void testGetCartTotalPriceBeforeDiscount() {
        user.addToCart("Product1", 2, 1, 100);
        user.addToCart("Product2", 1, 1, 50);
        assertEquals(150, user.getCartTotalPriceBeforeDiscount());
    }

    @Test
    public void testIsCartEmpty() {
        assertTrue(user.isCartEmpty());
        user.addToCart("Product1", 2, 1, 100);
        assertFalse(user.isCartEmpty());
    }
}
