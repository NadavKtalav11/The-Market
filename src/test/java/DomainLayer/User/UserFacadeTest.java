package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserFacadeTest {

    private UserFacade userFacade;
    private User mockUser;
    private Member mockMember;
    private final int userId = 1;
    private final int storeId = 1;
    private final String productName = "Product1";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() {
        userFacade = UserFacade.getInstance();
        mockUser = mock(User.class);
        mockMember = mock(Member.class);
        userFacade.allUsers.put(userId, mockUser);
    }

    @Test
    public void testGetInstance() {
        UserFacade instance = UserFacade.getInstance();
        assertNotNull(instance);
        assertSame(userFacade, instance);
    }

    @Test
    public void testGetUserByID() {
        assertEquals(mockUser, userFacade.getUserByID(userId));
    }

    @Test
    public void testIsUserLoggedIn() {
        when(mockUser.isLoggedIn()).thenReturn(true);
        assertTrue(userFacade.isUserLoggedIn(userId));
        verify(mockUser).isLoggedIn();
    }

    @Test
    public void testGetUsernameByUserID() {
        when(mockUser.getState()).thenReturn(mockMember);
        when(mockMember.getMemberID()).thenReturn(userId);

        assertEquals(userId, userFacade.getUsernameByUserID(userId));
        verify(mockMember).getMemberID();
    }

    @Test
    public void testExit() {
        doNothing().when(mockUser).exitMarketSystem();
        userFacade.exitMarketSystem(userId);
        verify(mockUser).exitMarketSystem();
        assertNull(userFacade.allUsers.get(userId));
    }

    @Test
    public void testAddItemsToBasket() {
        doNothing().when(mockUser).addToCart(anyString(), anyInt(), anyInt(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).addToCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testModifyBasketProduct() {
        doNothing().when(mockUser).modifyProductInCart(anyString(), anyInt(), anyInt(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).modifyProductInCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testCheckIfCanRemove() {
        when(mockUser.checkIfProductInUserCart(productName, storeId)).thenReturn(true);
        assertTrue(userFacade.checkIfCanRemove(productName, storeId, userId));
        verify(mockUser).checkIfProductInUserCart(productName, storeId);
    }

    @Test
    public void testRemoveItemFromUserCart() {
        doNothing().when(mockUser).removeItemFromUserCart(anyString(), anyInt());

        userFacade.removeItemFromUserCart(productName, storeId, userId);
        verify(mockUser).removeItemFromUserCart(productName, storeId);
    }

    @Test
    public void testRegister() throws Exception {
        String username = "testUser";
        String password = "testPass";
        String birthday = "01-01-2000";
        String address = "123 Test St";
        String country = "testCountry";
        String city = "testCity";
        String name = "testName";

        //doNothing().when(mockUser).register(anyString(), anyString(), anyString(), anyString());

        userFacade.register(userId, username, password, birthday,country,city ,address, name);
        //verify(mockUser).register(username, password, birthday, address);
    }

    @Test
    public void testLogin() throws Exception {
        String username = "testUser";
        String password = "testPass";

        Member member = new Member(userId, username, password, "1990-01-01", "Country", "City", "Address", "Name");
        doNothing().when(mockUser).Login(anyString(), anyString(), eq(member));

        // Assuming a method to add a member for testing
        userFacade.allUsers.put(userId, mockUser);
        userFacade.allUsers.put(2, new User(2)); // Adding another user for proper username verification
        ((User) userFacade.allUsers.get(2)).setState(member);

        userFacade.Login(userId, username, password);
        verify(mockUser).Login(username, password, member);
    }
}
