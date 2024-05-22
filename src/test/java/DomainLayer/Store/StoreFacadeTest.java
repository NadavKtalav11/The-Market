package DomainLayer.Store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class StoreFacadeTest {

    private StoreFacade storeFacade;

    @BeforeEach
    public void setUp() {
        storeFacade = StoreFacade.getInstance();
    }

    @Test
    public void testOpenStore() {
        int storeId = storeFacade.openStore();
        assertTrue(storeFacade.verifyStoreExist(storeId));
    }

    @Test
    public void testAddProductToStore() {
        int storeId = storeFacade.openStore();
        storeFacade.addProductToStore(storeId, "TestProduct", 100, 10);
        assertTrue(storeFacade.getStoreProducts(storeId).contains("TestProduct"));
    }

    @Test
    public void testRemoveProductFromStore() {
        int storeId = storeFacade.openStore();
        storeFacade.addProductToStore(storeId, "TestProduct", 100, 10);
        storeFacade.removeProductFromStore(storeId, "TestProduct");
        assertFalse(storeFacade.getStoreProducts(storeId).contains("TestProduct"));
    }

    @Test
    public void testUpdateProductInStore() {
        int storeId = storeFacade.openStore();
        storeFacade.addProductToStore(storeId, "TestProduct", 100, 10);
        storeFacade.updateProductInStore(storeId, "TestProduct", 150, 20);
        Product product = storeFacade.getStoreByID(storeId).getProductByName("TestProduct");
        assertEquals(150, product.getPrice());
        assertEquals(20, product.getQuantity());
    }

    @Test
    public void testCheckQuantityAndPolicies() {
        int storeId = storeFacade.openStore();
        storeFacade.addProductToStore(storeId, "TestProduct", 100, 10);
        assertDoesNotThrow(() -> storeFacade.checkQuantityAndPolicies("TestProduct", 5, storeId, 123));
    }

    @Test
    public void testCalcPrice() {
        int storeId = storeFacade.openStore();
        storeFacade.addProductToStore(storeId, "TestProduct", 100, 10);
        assertEquals(500, storeFacade.calcPrice("TestProduct", 5, storeId, 123));
    }

    @Test
    public void testCloseStore() {
        int storeId = storeFacade.openStore();
        storeFacade.closeStore(storeId);
        assertFalse(storeFacade.getStoreByID(storeId).getIsOpened());
    }

    @Test
    public void testGetInformationAboutOpenStores() {
        int storeId1 = storeFacade.openStore();
        int storeId2 = storeFacade.openStore();
        List<Integer> openStores = storeFacade.getInformationAboutOpenStores();
        assertTrue(openStores.contains(storeId1));
        assertTrue(openStores.contains(storeId2));
    }

    @Test
    public void testGetInformationAboutClosedStores() {
        int storeId1 = storeFacade.openStore();
        int storeId2 = storeFacade.openStore();
        storeFacade.closeStore(storeId1);
        List<Integer> closedStores = storeFacade.getInformationAboutClosedStores();
        assertTrue(closedStores.contains(storeId1));
        assertFalse(closedStores.contains(storeId2));
    }
}
