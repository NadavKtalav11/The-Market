package DomainLayer.Store;

import DomainLayer.User.Member;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import ServiceLayer.Response;
import Util.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class StoreFacadeTest {

    private StoreFacade storeFacade;
    private Store mockStore;
    private final int userId = 1;
    private final int storeId = 0;
    private final String productName = "Product1";
    private final int quantity = 2;
    private final int totalPrice = 100;

    @BeforeEach
    public void setUp() {
        storeFacade = StoreFacade.getInstance();
        mockStore = mock(Store.class);
        storeFacade.openStore("Grocery", "");
    }

    @Test
    void testOpenStore() {
        int storeId = storeFacade.openStore("Grocery", "");
        assertNotNull(storeFacade.getStoreByID(storeId));
    }

    @Test
    void testGetStoreByID() {
        Store store = storeFacade.getStoreByID(storeId);
        assertNotNull(store);
        assertEquals(storeId, store.getStoreID());
    }

    @Test
    void testAddProductToStore() throws Exception {
        storeFacade.addProductToStore(storeId, new ProductDTO("Product1", 100, 10, "Description1", "Category1"));
        verify(mockStore).addProduct(new ProductDTO("Product1", 100, 10, "Description1", "Category1"));

        storeFacade.addProductToStore(storeId, new ProductDTO("Product2", 100, 10, "Description1", "Category1"));
        verify(mockStore).addProduct(new ProductDTO("Product2", 100, 10, "Description1", "Category1"));

        storeFacade.addProductToStore(storeId, new ProductDTO("Product3", 100, 10, "Description1", "Category1"));
        verify(mockStore).addProduct(new ProductDTO("Product3", 100, 10, "Description1", "Category1"));
    }

    @Test
    void testRemoveProductFromStore() throws Exception {
        when(mockStore.checkProductExists(anyString())).thenReturn(true);
        storeFacade.removeProductFromStore(storeId, "Product1");
        verify(mockStore).removeProduct("Product1");

        storeFacade.removeProductFromStore(storeId, "Product2");
        verify(mockStore).removeProduct("Product2");

        storeFacade.removeProductFromStore(storeId, "Product3");
        verify(mockStore).removeProduct("Product3");
    }

    @Test
    void testInStoreProductSearch() {
        List<String> expectedProducts = Arrays.asList("Milk", "Cheese");
        when(mockStore.matchProducts(anyString(), anyString(), anyList())).thenReturn(expectedProducts);

        List<String> result = storeFacade.inStoreProductSearch("Milk", "Dairy", Arrays.asList("Fresh"), storeId);

        assertEquals(expectedProducts, result);
    }

    @Test
    void testCheckQuantityAndPolicies() {
        when(mockStore.checkProductExists("Milk")).thenReturn(true);
        when(mockStore.checkProductQuantity("Milk", 5)).thenReturn(true);
        when(mockStore.checkPurchasePolicy(1, "Milk")).thenReturn(true);
        when(mockStore.checkDiscountPolicy(1, "Milk")).thenReturn(true);


        //if no exceptions thrown, this test passes
        storeFacade.checkQuantityAndPolicies("Milk", 5, 0, 1);

        verify(mockStore).checkProductExists("Milk");
        verify(mockStore).checkProductQuantity("Milk", 5);
        verify(mockStore).checkPurchasePolicy(1, "Milk");
        verify(mockStore).checkDiscountPolicy(1, "Milk");

        //fail case for the test
        when(mockStore.checkProductExists("Milk")).thenReturn(false);
        assertThrows(Exception.class, () -> storeFacade.checkQuantityAndPolicies("Milk", 5, 0, 1));

    }

    @Test
    void testCalcPrice() {
        when(mockStore.calcPriceInStore("Milk", 5, 1)).thenReturn(100);

        int price = storeFacade.calcPrice("Milk", 5, 0, 1);

        assertEquals(100, price);
        verify(mockStore).calcPriceInStore("Milk", 5, 1);
    }

    @Test
    void testUpdateProductInStore() throws Exception {
        when(mockStore.checkProductExists(anyString())).thenReturn(true);
        storeFacade.updateProductInStore(0, new ProductDTO("Milk", 100, 10, "Fresh Milk", "Dairy"));
        verify(mockStore).updateProduct(new ProductDTO("Milk", 100, 10, "Fresh Milk", "Dairy"));
    }

    @Test
    void testVerifyStoreExist() {
        boolean exists = storeFacade.verifyStoreExist(0);

        assertTrue(exists);
    }

    @Test
    void testCloseStore() throws Exception {
        storeFacade.closeStore(0);

        verify(mockStore).closeStore();
    }

    @Test
    void testGetInformationAboutOpenStores() {
        when(mockStore.getIsOpened()).thenReturn(true);

        List<Integer> openStores = storeFacade.getInformationAboutOpenStores();

        assertTrue(openStores.contains(0));
    }

    @Test
    void testGetInformationAboutClosedStores() {
        when(mockStore.getIsOpened()).thenReturn(false);

        List<Integer> closedStores = storeFacade.getInformationAboutClosedStores();

        assertTrue(closedStores.contains(0));
    }

    @Test
    void testGetStoreProducts() {
        List<String> products = Arrays.asList("Milk", "Cheese");
        when(mockStore.getProducts()).thenReturn(products);

        List<String> result = storeFacade.getStoreProducts(0);

        assertEquals(products, result);
    }

    @Test
    void testInStoreProductFilter() {
        List<String> expectedProducts = Arrays.asList("Milk", "Cheese");
        when(mockStore.filterProducts("Dairy", Arrays.asList("Fresh"), 10, 100, 4.5, Arrays.asList("Milk", "Cheese"), 4.0)).thenReturn(expectedProducts);

        List<String> result = storeFacade.inStoreProductFilter("Dairy", Arrays.asList("Fresh"), 10, 100, 4.5, 0, Arrays.asList("Milk", "Cheese"), 4.0);

        assertEquals(expectedProducts, result);
        verify(mockStore).filterProducts("Dairy", Arrays.asList("Fresh"), 10, 100, 4.5, Arrays.asList("Milk", "Cheese"), 4.0);
    }

    @Test
    void testCheckCategory() {
        assertFalse(storeFacade.checkCategory("NonExistentCategory"));
    }

    @Test
    void testCheckProductExistInStore() {
        when(mockStore.checkProductExists("NonExistentProduct")).thenReturn(false);
        assertFalse(storeFacade.checkProductExistInStore("NonExistentProduct", 0));
    }

    @Test
    void testGetStores() {
        List<Integer> stores = storeFacade.getStores();

        assertTrue(stores.contains(0));
    }

    @Test
    void testAddReceiptToStore() {
        storeFacade.addReceiptToStore(0, 1, 1);

        verify(mockStore).addReceipt(1, 1);
    }

}
