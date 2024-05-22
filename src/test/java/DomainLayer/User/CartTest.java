package DomainLayer.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CartTest {

    private Cart cart;
    private Basket mockBasket;

    @BeforeEach
    public void setUp() {
        cart = new Cart();
        mockBasket = mock(Basket.class);
    }

    @Test
    public void testCartInitialization() {
        assertEquals(0, cart.getCartPrice());
        assertNotNull(cart.baskets);
        assertTrue(cart.baskets.isEmpty());
    }

    @Test
    public void testAddItemsToCart() {
        String productName = "Product1";
        int quantity = 1;
        int storeId = 100;
        int totalPrice = 50;

        cart.addItemsToCart(productName, quantity, storeId, totalPrice);
        assertTrue(cart.baskets.containsKey(storeId));
        Basket basket = cart.baskets.get(storeId);
        assertNotNull(basket);
        assertEquals(storeId, basket.getStoreId());
    }

    @Test
    public void testModifyProductInCart() {
        String productName = "Product1";
        int quantity = 2;
        int storeId = 100;
        int totalPrice = 100;

        cart.baskets.put(storeId, mockBasket);
        cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
        verify(mockBasket).modifyProduct(productName, quantity, totalPrice);
    }

    @Test
    public void testModifyProductInNonExistentCart() {
        String productName = "Product1";
        int quantity = 2;
        int storeId = 100;
        int totalPrice = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            cart.modifyProductInCart(productName, quantity, storeId, totalPrice);
        });
    }

    @Test
    public void testCalcCartTotal() {
        int storeId = 100;

        when(mockBasket.getBasketPrice()).thenReturn(50);
        cart.baskets.put(storeId, mockBasket);

        cart.calcCartTotal();
        assertEquals(50, cart.getCartPrice());
    }

    @Test
    public void testCheckIfProductInCart() {
        String productName = "Product1";
        int storeId = 100;

        when(mockBasket.checkIfProductInBasket(productName)).thenReturn(true);
        cart.baskets.put(storeId, mockBasket);

        assertTrue(cart.checkIfProductInCart(productName, storeId));
        verify(mockBasket).checkIfProductInBasket(productName);
    }

    @Test
    public void testCheckIfProductInNonExistentCart() {
        String productName = "Product1";
        int storeId = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            cart.checkIfProductInCart(productName, storeId);
        });
    }

    @Test
    public void testRemoveItemFromCart() {
        String productName = "Product1";
        int storeId = 100;

        cart.baskets.put(storeId, mockBasket);
        cart.removeItemFromCart(productName, storeId);
        verify(mockBasket).removeItemFromBasket(productName);
    }

    @Test
    public void testRemoveItemFromNonExistentCart() {
        String productName = "Product1";
        int storeId = 100;

        assertThrows(IllegalArgumentException.class, () -> {
            cart.removeItemFromCart(productName, storeId);
        });
    }
}
