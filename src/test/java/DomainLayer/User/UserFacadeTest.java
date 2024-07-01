package DomainLayer.User;


import DomainLayer.Repositories.MemberRepository;
import DomainLayer.Repositories.UserRepository;
import Util.UserDTO;
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
    private Member mockMember;
    private UserRepository mockUserRepository;  // Mocking the UserRepository
    @InjectMocks
    private UserFacade userFacade;  // Ensure this is injected with mocks
    private MemberRepository mockMemberRepository;  // Mocking the MemberRepository
    private final String userId = "1";
    private final String storeId = "1";
    private final String productName = "Product1";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Configure mockUser to return a specific userId when getUserID() is called
        when(mockUser.getUserID()).thenReturn("1");

        // Reset the UserFacade singleton for each test
        userFacade = UserFacade.getInstance();
        mockMemberRepository = mock(MemberRepository.class);
        mockUserRepository = mock(UserRepository.class);
        mockMember = mock(Member.class);
        userFacade.userRepository.clear();
        userFacade.members.clear();

        // Insert the mockUser into the UserFacade for userId = 1
        userFacade.userRepository.add("1", mockUser);  // Assuming userId = 1
    }

    @Test
    public void testGetInstance() {
        UserFacade instance = UserFacade.getInstance();
        assertNotNull(instance);
        assertSame(userFacade, instance);
    }

    @Test
    public void testGetUserByID() {
        assertEquals(mockUser, userFacade.getUserByID("1"));
    }

    @Test
    public void testAddUser() {
        String userId = userFacade.addUser();
        assertNotNull(userFacade.getUserByID(userId));
    }

    @Test
    public void testRegister() throws Exception {
        String userId = userFacade.addUser();
        userFacade.register(userId, new UserDTO(userId, "testUser", "01/01/2000", "Test Country", "Test City", "123 Test St", "Test Name"), "testPass");

        Member member = userFacade.getMemberByUsername("testUser");
        assertNotNull(member);
        assertEquals("testUser", member.getUsername());
    }

    @Test
    public void testAddItemsToBasket() {
        doNothing().when(mockUser).addToCart(anyString(), anyInt(), anyString(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).addToCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testModifyBasketProduct() {
        doNothing().when(mockUser).modifyProductInCart(anyString(), anyInt(), anyString(), anyInt());
        doNothing().when(mockUser).updateCartPrice();

        userFacade.modifyBasketProduct(productName, quantity, storeId, userId, totalPrice);
        verify(mockUser).modifyProductInCart(productName, quantity, storeId, totalPrice);
        verify(mockUser).updateCartPrice();
    }

    @Test
    public void testCheckIfCanRemove() {
        when(mockUser.checkIfProductInUserCart(productName, storeId)).thenReturn(true);
        //test that the method didnt throw an exception
        assertDoesNotThrow(() -> userFacade.checkIfCanRemove(productName, storeId, userId));
        verify(mockUser).checkIfProductInUserCart(productName, storeId);
    }

    @Test
    public void testRemoveItemFromUserCart() {
        doNothing().when(mockUser).removeItemFromUserCart(anyString(), anyString());

        userFacade.removeItemFromUserCart(productName, storeId, userId);
        verify(mockUser).removeItemFromUserCart(productName, storeId);
    }
}
