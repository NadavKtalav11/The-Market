package IntegrationTests.Store;

import DomainLayer.Store.*;
import Util.*;
import org.jose4j.jwk.Use;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoreFacadeTest {
    private StoreFacade storeFacade;

    @BeforeEach
    public void setUp() {
        storeFacade = StoreFacade.getInstance().newForTest();
    }

    @Test
    public void testOpenStore() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        Store store = storeFacade.getStoreByID(storeId);

        assertNotNull(store);
        assertEquals("Test Store", store.getStoreName());
        assertEquals("A store for testing", store.getDescription());
    }

    @Test
    public void testGetStoreByID() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        Store store = storeFacade.getStoreByID(storeId);

        assertNotNull(store);
        assertEquals("Test Store", store.getStoreName());
    }

    @Test
    public void testAddProductToStore() throws Exception {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        storeFacade.addProductToStore(storeId, product);

        Store store = storeFacade.getStoreByID(storeId);
        assertTrue(store.checkProductExists("Product1"));
    }

    @Test
    public void testRemoveProductFromStore() throws Exception {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        storeFacade.addProductToStore(storeId, product);

        storeFacade.removeProductFromStore(storeId, "Product1");

        Store store = storeFacade.getStoreByID(storeId);
        assertFalse(store.checkProductExists("Product1"));
    }

    @Test
    public void testUpdateProductInStore() throws Exception {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        storeFacade.addProductToStore(storeId, product);

        ProductDTO updatedProduct = new ProductDTO("Product1", 20, 10, "Updated product", "BOOKS");
        storeFacade.updateProductInStore(storeId, updatedProduct);

        Store store = storeFacade.getStoreByID(storeId);
        ProductDTO retrievedProduct = store.getProductDTOByName("Product1", 10);

        assertEquals(20, retrievedProduct.getPrice());
        assertEquals(10, retrievedProduct.getQuantity());
        assertEquals("Updated product", retrievedProduct.getDescription());
        assertEquals("BOOKS", retrievedProduct.getCategoryStr());
    }

    @Test
    public void testGetStoreProductsDTO() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        List<ProductDTO> productsDTO = storeFacade.getStoreProductsDTO(storeId);

        assertEquals(1, productsDTO.size());
        assertEquals("Product1", productsDTO.get(0).getName());
    }

    @Test
    public void testReturnProductToStore() throws Exception {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        Map<String, List<Integer>> products = new HashMap<>();
        products.put("Product1", Collections.singletonList(5));

        storeFacade.returnProductToStore(products, storeId);

        Store store = storeFacade.getStoreByID(storeId);
        List<ProductDTO> productDTOS = store.getProductsDTO();
        assertEquals(10, productDTOS.get(0).getQuantity());  // Original quantity was 5, added 5 more, should be 10
    }

    @Test
    public void testCloseStore() throws Exception {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");

        storeFacade.closeStore(storeId);

        Store store = storeFacade.getStoreByID(storeId);
        assertFalse(store.getIsOpened());
    }

    @Test
    public void testGetAllDTOs() {
        String storeId1 = storeFacade.openStore("Test Store 1", "A store for testing");
        String storeId2 = storeFacade.openStore("Test Store 2", "Another store for testing");

        List<StoreDTO> storeDTOs = storeFacade.getAllDTOs();

        assertEquals(2, storeDTOs.size());
    }

    @Test
    public void testCheckQuantity() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        storeFacade.checkQuantity("Product1", 5, storeId);

        // If no exception is thrown, the test passes
    }

    @Test
    public void testCheckPurchasePolicy() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("Product1", 10, 5, "A product", "TOYS"));

        storeFacade.checkPurchasePolicy(user, products, storeId);

        // If no exception is thrown, the test passes
    }

    @Test
    public void testCalcDiscountPolicy() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        UserDTO user = new UserDTO("User1", "user1@gmail.com", "12/3/45", "Israel", "Ashqelon", "rabin", "moshe");
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("Product1", 10, 5, "A product", "TOYS"));

        int discount = storeFacade.calcDiscountPolicy(user, products, storeId);

        assertEquals(0, discount); // Assuming no discount policies are applied in the test setup
    }

    @Test
    public void testCalcPrice() {
        String storeId = storeFacade.openStore("Test Store", "A store for testing");
        ProductDTO product = new ProductDTO("Product1", 10, 5, "A product", "TOYS");
        try {
            storeFacade.addProductToStore(storeId, product);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }

        int price = storeFacade.calcPrice("Product1", 5, storeId, "User1");

        assertEquals(50, price); // Price = quantity * unit price
    }
}
