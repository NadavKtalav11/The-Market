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

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.enterMarketSystem();
        impl.register("0", "saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.register("1", "tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.login("0", "saar", "fadida");
        impl.login("1", "tom", "shlaifer");
        impl.openStore("0", "alona", "shopping");
        impl.appointStoreManager("0", "tom", "0", true, false);
        impl.addProductToStore("0", "0", "weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore("1","0","weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.removeProductFromStore("1","0","skirt").isSuccess());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions("0","tom","0",false,false);
        Exception exception = assertThrows(Exception.class, () -> {
            Response<String> response = impl.removeProductFromStore("1","0","weddingDress");
            assertFalse(response.isSuccess());
        });
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), exception.getMessage());
    }
}
