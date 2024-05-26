package DomainLayer.Store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StoreTest {

    private Store store;
    private final int storeId = 1;
    private final String productName = "TestProduct";
    private final int price = 100;
    private final int quantity = 10;


    @BeforeEach
    public void setUp() {
        store = new Store(storeId);
        store.addProduct(productName, price, quantity, "good", "drink"  );
    }

    @Test
    public void testGetStoreID() {
        assertEquals(storeId, store.getStoreID());
    }

    @Test
    public void testAddProduct() {
        assertTrue(store.checkProductExists(productName));
    }

    @Test
    public void testGetProductByName() {
        Product product = store.getProductByName(productName);
        assertNotNull(product);
        assertEquals(productName, product.getProductName());
    }

    @Test
    public void testCheckProductQuantity() {
        assertTrue(store.checkProductQuantity(productName, quantity));
        assertFalse(store.checkProductQuantity(productName, quantity + 1));
    }

    @Test
    public void testCalcPriceInStore() {
        assertEquals(price * 5, store.calcPriceInStore(productName, 5, 123));
    }

    @Test
    public void testRemoveProduct() {
        store.removeProduct(productName);
        assertFalse(store.checkProductExists(productName));
    }

    @Test
    public void testUpdateProduct() {
        int newPrice = 150;
        int newQuantity = 20;
        //store.updateProduct(productName, newPrice, newQuantity);
        assertEquals(newPrice, store.getProductByName(productName).getPrice());
        assertEquals(newQuantity, store.getProductByName(productName).getQuantity());
    }

    @Test
    public void testCloseStore() {
        store.closeStore();
        assertFalse(store.getIsOpened());
    }

    @Test
    public void testGetProducts() {
        assertEquals(1, store.getProducts().size());
        assertTrue(store.getProducts().contains(productName));
    }
}
