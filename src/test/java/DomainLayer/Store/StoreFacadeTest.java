package DomainLayer.Store;

import DomainLayer.User.Member;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
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
        storeFacade.allStores.put(0, mockStore);
    }

    @Test
    void testOpenStore() {
        //int storeId = storeFacade.openStore();
        //assertNotNull(storeFacade.getStoreByID(storeId));
    }

    @Test
    void testGetStoreByID() {
        Store store = storeFacade.getStoreByID(storeId);
        assertNotNull(store);
        assertEquals(storeId, store.getStoreID());
    }

    @Test
    void testAddProductToStore() {
        storeFacade.addProductToStore(storeId, "Product1", 100, 10, "Description1", "Category1");
        verify(mockStore).addProduct("Product1", 100, 10, "Description1", "Category1");

        storeFacade.addProductToStore(storeId, "Product2", 100, 10, "Description1", "Category1");
        verify(mockStore).addProduct("Product2", 100, 10, "Description1", "Category1");

        storeFacade.addProductToStore(storeId, "Product3", 100, 10, "Description1", "Category1");
        verify(mockStore).addProduct("Product3", 100, 10, "Description1", "Category1");
    }

    @Test
    void testRemoveProductFromStore() {
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

        boolean result = storeFacade.checkQuantityAndPolicies("Milk", 5, 0, 1);

        assertTrue(result);
        verify(mockStore).checkProductExists("Milk");
        verify(mockStore).checkProductQuantity("Milk", 5);
        verify(mockStore).checkPurchasePolicy(1, "Milk");
        verify(mockStore).checkDiscountPolicy(1, "Milk");
    }

    @Test
    void testCalcPrice() {
        when(mockStore.calcPriceInStore("Milk", 5, 1)).thenReturn(100);

        int price = storeFacade.calcPrice("Milk", 5, 0, 1);

        assertEquals(100, price);
        verify(mockStore).calcPriceInStore("Milk", 5, 1);
    }

    @Test
    void testUpdateProductInStore() {
        storeFacade.updateProductInStore(0, "Milk", 100, 10, "Fresh Milk", "Dairy");

        verify(mockStore).updateProduct("Milk", 100, 10, "Fresh Milk", "Dairy");
    }

    @Test
    void testVerifyStoreExist() {
        boolean exists = storeFacade.verifyStoreExist(0);

        assertTrue(exists);
    }

    @Test
    void testCloseStore() {
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
        assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.checkCategory("NonExistentCategory");
        });
    }

    @Test
    void testCheckProductExistInStore() {
        when(mockStore.checkProductExists("NonExistentProduct")).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            storeFacade.checkProductExistInStore("NonExistentProduct", 0);
        });
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
