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

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.login(saarUserID, "saar", "fadida");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.addProductToStore(saarUserID, "0","weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore(saarUserID, "0","weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.removeProductFromStore(saarUserID, "0","skirt");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }
}
