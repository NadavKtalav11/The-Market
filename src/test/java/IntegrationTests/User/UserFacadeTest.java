package IntegrationTests.User;

import DomainLayer.User.Member;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserFacadeTest {

    private UserFacade userFacade;
    private final String userId = "1";
    private final String storeId = "1";
    private final String productName = "Product1";
    private final String productName2 = "Product2";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() {
        // Reset the UserFacade singleton for each test
        userFacade = UserFacade.getInstance();
        userFacade.getUserRepository().clear();
        userFacade.getMembers().clear();

        // Create a new user and add to the UserFacade
        User user = new User(userId);
        userFacade.getUserRepository().add(userId, user);
    }

    @Test
    public void testGetInstance() {
        UserFacade instance = UserFacade.getInstance();
        assertNotNull(instance);
        assertSame(userFacade, instance);
    }

    @Test
    public void testGetUserByID() {
        User user = userFacade.getUserByID(userId);
        assertNotNull(user);
        assertEquals(userId, user.getUserID());
    }

    @Test
    public void testAddUser() {
        String newUserId = userFacade.addUser();
        User newUser = userFacade.getUserByID(newUserId);
        assertNotNull(newUser);
        assertEquals(newUserId, newUser.getUserID());
    }

    @Test
    public void testRegister() throws Exception {
        String newUserId = userFacade.addUser();
        UserDTO userDTO = new UserDTO(newUserId, "testUser", "01/01/2000", "Test Country", "Test City", "123 Test St", "Test Name");
        userFacade.register(newUserId, userDTO, "testPass");

        Member member = userFacade.getMemberByUsername("testUser");
        assertNotNull(member);
        assertEquals("testUser", member.getUsername());
    }

    @Test
    public void testAddItemsToBasket() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

        User user = userFacade.getUserByID(userId);
        assertTrue(user.checkIfProductInUserCart(productName, storeId));
        assertEquals(1, user.getCartProductsByStore(storeId).size());
    }

    @Test
    public void testModifyBasketProduct() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        userFacade.modifyBasketProduct(productName, quantity + 1, storeId, userId, totalPrice + 50);

        User user = userFacade.getUserByID(userId);
        assertEquals(3, user.getCartProductsByStore(storeId).get(productName).get(0));
        assertEquals(150, user.getCartProductsByStore(storeId).get(productName).get(1));
    }

    @Test
    public void testCheckIfCanRemove() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);

        assertDoesNotThrow(() -> userFacade.checkIfCanRemove(productName, storeId, userId));
    }

    @Test
    public void testRemoveItemFromUserCart() {
        userFacade.addItemsToBasket(productName, quantity, storeId, userId, totalPrice);
        userFacade.addItemsToBasket(productName2, quantity, storeId, userId, totalPrice);

        userFacade.removeItemFromUserCart(productName, storeId, userId);

        User user = userFacade.getUserByID(userId);
        assertFalse(user.checkIfProductInUserCart(productName, storeId));
    }
}
