package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        tomUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID, "saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.register(tomUserID, "tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.login(saarUserID, "saar", "fadida");
        impl.login(tomUserID, "tom", "shlaifer");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreManager(saarUserID, "tom", "0", true, false);
        impl.addProductToStore(saarUserID, "0", "weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore(tomUserID,"0","weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.removeProductFromStore(tomUserID,"0","skirt");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom","0",false,false);
        Response<String> response = impl.removeProductFromStore(tomUserID,"0","weddingDress");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
