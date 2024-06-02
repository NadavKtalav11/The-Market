package DomainLayer.Store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class StoreTest {

    private Store store;
    private final String storeId = "1";
    private Product mockProduct;
    private final String productName = "TestProduct";
    private final int price = 100;
    private final int quantity = 10;


    @BeforeEach
    public void setUp() {
        store = new Store(storeId, "Grocery", "");
        mockProduct = mock(Product.class);
    }

    @Test
    void testGetProductByName() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        Product product = store.getProductByName("Milk");

        assertNotNull(product);
        assertEquals("Milk", product.getProductName());
    }

    @Test
    void testAddProduct() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        Product product = store.getProductByName("Milk");

        assertNotNull(product);
        assertEquals("Milk", product.getProductName());
        assertEquals(10, product.getPrice());
        assertEquals(100, product.getQuantity());
        assertEquals("Dairy product", product.getDescription());
        assertEquals(Category.DAIRY.toString(), product.getCategoryName());
    }

    @Test
    void testCheckProductExists() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        assertTrue(store.checkProductExists("Milk"));
        assertFalse(store.checkProductExists("Cheese"));
    }

    @Test
    void testCheckProductQuantity() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        assertTrue(store.checkProductQuantity("Milk", 50));
        assertFalse(store.checkProductQuantity("Milk", 150));
    }

    @Test
    void testCalcPriceInStore() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        int price = store.calcPriceInStore("Milk", 5, "1");

        assertEquals(50, price);
    }

    @Test
    void testRemoveProduct() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        store.removeProduct("Milk");

        assertFalse(store.checkProductExists("Milk"));
    }

    @Test
    void testUpdateProduct() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        store.updateProduct("Milk", 15, 200, "Updated description", "food");

        Product product = store.getProductByName("Milk");
        assertEquals(15, product.getPrice());
        assertEquals(200, product.getQuantity());
        assertEquals("Updated description", product.getDescription());
        assertEquals(Category.FOOD.toString(), product.getCategoryName());
    }

    @Test
    void testCloseStore() {
        store.closeStore();
        assertFalse(store.getIsOpened());
    }

    @Test
    void testGetIsOpened() {
        assertTrue(store.getIsOpened());
    }

    @Test
    void testGetProducts() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        store.addProduct("Cheese", 20, 50, "Dairy product", "Dairy");

        List<String> products = store.getProducts();

        assertEquals(2, products.size());
        assertTrue(products.contains("Milk"));
        assertTrue(products.contains("Cheese"));
    }

    @Test
    void testMatchProducts() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        store.addProduct("Cheese", 20, 50, "Dairy product", "Dairy");

        List<String> matchedProducts1 = store.matchProducts("Milk", null, null);

        assertEquals(1, matchedProducts1.size());
        assertTrue(matchedProducts1.contains("Milk"));

        List<String> matchedProducts2 = store.matchProducts(null, "Dairy", null);

        assertEquals(2, matchedProducts2.size());
        assertTrue(matchedProducts2.contains("Milk"));
        assertTrue(matchedProducts2.contains("Cheese"));
    }

    @Test
    void testFilterProducts() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        store.addProduct("Cheese", 20, 50, "Dairy product", "Dairy");

        List<String> productsFromSearch = Arrays.asList("Milk", "Cheese");
        List<String> filteredProducts = store.filterProducts("Dairy", null, 5, 15, null, productsFromSearch, null);

        assertEquals(1, filteredProducts.size());
        assertTrue(filteredProducts.contains("Milk"));
    }

    @Test
    void testReturnProductToStore() {
        store.addProduct("Milk", 10, 100, "Dairy product", "Dairy");
        Map<String, Integer> productsToReturn = new HashMap<>();
        productsToReturn.put("Milk", 10);

        store.returnProductToStore(productsToReturn);

        Product product = store.getProductByName("Milk");
        assertEquals(110, product.getQuantity());
    }

}