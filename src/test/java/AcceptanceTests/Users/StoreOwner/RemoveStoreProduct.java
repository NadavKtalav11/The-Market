package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String storeID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeID= impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeID,"weddingDress", 10, 5, "pink", "CLOTHING");
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore(saarUserID, storeID,"weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.removeProductFromStore(saarUserID, storeID,"skirt");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }
}
