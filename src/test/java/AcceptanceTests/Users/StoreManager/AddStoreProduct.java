package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.login(saarUserID, "saar", "fadida");
        impl.login(tomUserID, "tom", "shlaifer");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreManager(saarUserID, "tom", "0", true, false);
        impl.addProductToStore(saarUserID, "0","weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(tomUserID,"0","heels", 4, 2, "black", "shoes").isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        Response<String> response = impl.addProductToStore(tomUserID,"0","weddingDress", 3, 6, "pink", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productAlreadyExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.addProductToStore(tomUserID,"0","shirt", 5, -4, "green", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom","0",false,false);
        Response<String> response = impl.addProductToStore(tomUserID,"0","heels", 3, 3, "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
