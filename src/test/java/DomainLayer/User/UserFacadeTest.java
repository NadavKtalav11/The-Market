package DomainLayer.User;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserFacadeTest {
    @Mock
    private User mockUser;
    @Mock
    private Member mockMember;
    @InjectMocks
    private UserFacade userFacade;  // Ensure this is injected with mocks

    private final int userId = 1;
    private final int storeId = 1;
    private final String productName = "Product1";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Configure mockUser to return a specific userId when getUserID() is called
        when(mockUser.getUserID()).thenReturn(1);

        // Reset the UserFacade singleton for each test
        userFacade = UserFacade.getInstance();
        userFacade.allUsers.clear();
        userFacade.members.clear();

        // Insert the mockUser into the UserFacade for userId = 1
        userFacade.allUsers.put(1, mockUser);  // Assuming userId = 1
    }

    @Test
    public void testGetInstance() {
        UserFacade instance = UserFacade.getInstance();
        assertNotNull(instance);
        assertSame(userFacade, instance);
    }

    @Test
    public void testGetUserByID() {
        assertEquals(mockUser, userFacade.getUserByID(1));
    }

    @Test
    public void testIsUserLoggedIn() {
        when(mockUser.isLoggedIn()).thenReturn(true);
        assertTrue(userFacade.isUserLoggedIn(1));
    }

    @Test
    public void testAddUser() {
        int userId = userFacade.addUser();
        assertNotNull(userFacade.getUserByID(userId));
        assertEquals(userId, userFacade.getCurrentUserID() - 1);
    }

    @Test
    public void testRegister() throws Exception {
        int userId = userFacade.addUser();
        userFacade.register(userId, "testUser", "testPass", "01-01-2000", "Test Country", "Test City", "123 Test St", "Test Name");

        Member member = userFacade.getMemberByUsername("testUser");
        assertNotNull(member);
        assertEquals("testUser", member.getUsername());
    }

    @Test
    public void testLogin() {
        //already checked in userTest
    }


    @Test
    public void testGetUsernameByUserID() {
        when(mockUser.getState()).thenReturn(mockMember);
        when(mockMember.getMemberID()).thenReturn(userId);

        assertEquals(userId, userFacade.getUsernameByUserID(userId));
        verify(mockMember).getMemberID();
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

//    @Test
//    public void testRegister() throws Exception {
//        String username = "testUser";
//        String password = "testPass";
//        String birthday = "01-01-2000";
//        String address = "123 Test St";
//        String country = "testCountry";
//        String city = "testCity";
//        String name = "testName";
//
//        //doNothing().when(mockUser).register(anyString(), anyString(), anyString(), anyString());
//
//        userFacade.register(userId, username, password, birthday,country,city ,address, name);
//        //verify(mockUser).register(username, password, birthday, address);
//    }

//    @Test
//    public void testLogin() throws Exception {
//        String username = "testUser";
//        String password = "testPass";
//
//        Member member = new Member(userId, username, password, "1990-01-01", "Country", "City", "Address", "Name");
//        doNothing().when(mockUser).Login(anyString(), anyString(), eq(member));
//
//        // Assuming a method to add a member for testing
//        userFacade.allUsers.put(userId, mockUser);
//        userFacade.allUsers.put(2, new User(2)); // Adding another user for proper username verification
//        ((User) userFacade.allUsers.get(2)).setState(member);
//
//        userFacade.Login(userId, username, password);
//        verify(mockUser).Login(username, password, member);
//    }
}
