package DomainLayer.User;

import DomainLayer.User.Cart;
import DomainLayer.User.Guest;
import DomainLayer.User.Member;
import DomainLayer.User.State;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTest {

    private User user;
    private Cart mockCart;
    private State mockState;
    private int userID = 1;

    @BeforeEach
    public void setUp() {
        mockCart = Mockito.mock(Cart.class);
        mockState = Mockito.mock(State.class);

        //user = new User(userID);
        user.setCart(mockCart); // Inject the mock cart
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
        user.setState(mockState);
        user.Logout();
        verify(mockState).Logout(user);
    }

    @Test
    public void testExit() {
        user.setState(mockState);
        user.exitMarketSystem();
        verify(mockState).exitMarketSystem(user);
    }

    @Test
    public void testAddToCart() {
        String productName = "Product1";
        int quantity = 1;
        int storeId = 100;
        int totalPrice = 50;

        user.addToCart(productName, quantity, storeId, totalPrice);
        verify(mockCart).addItemsToCart(productName, quantity, storeId, totalPrice);
    }

    @Test
    public void testModifyProductInCart() {
        String productName = "Product1";
        int quantity = 2;
        int storeId = 100;
        int totalPrice = 100;

        user.modifyProductInCart(productName, quantity, storeId, totalPrice);
        verify(mockCart).modifyProductInCart(productName, quantity, storeId, totalPrice);
    }

    @Test
    public void testUpdateCartPrice() {
        user.updateCartPrice();
        verify(mockCart).calcCartTotal();
    }

    @Test
    public void testRegister() throws Exception {
        String username = "user";
        String password = "pass";
        String birthday = "01-01-2000";
        String address = "123 Street";

        user.setState(mockState);
        //user.register(username, password, birthday, address);
        //verify(mockState).Register(user, username, password, birthday, address);
    }

    @Test
    void testLogin() throws Exception {
        String username = "user";
        String password = "pass";

        user.Login(username, password, new Member(1, username, password, "1990-01-01", "Country", "City", "Address", "Name"));
        verify(mockState).Login(eq(user), eq(username), eq(password), any(Member.class));
    }

    @Test
    public void testIsLoggedIn() {
        assertFalse(user.isLoggedIn());
        user.setState(new Member(1,"username1", "password" , "1.2.2020", "israel" , "bash", "bialik", "noa"));
        assertTrue(user.isLoggedIn());
    }

    @Test
    public void testCheckIfProductInUserCart() {
        String productName = "Product1";
        int storeId = 100;

        when(mockCart.checkIfProductInCart(productName, storeId)).thenReturn(true);
        assertTrue(user.checkIfProductInUserCart(productName, storeId));
        verify(mockCart).checkIfProductInCart(productName, storeId);
    }

    @Test
    public void testRemoveItemFromUserCart() {
        String productName = "Product1";
        int storeId = 100;

        user.removeItemFromUserCart(productName, storeId);
        verify(mockCart).removeItemFromCart(productName, storeId);
    }
}
