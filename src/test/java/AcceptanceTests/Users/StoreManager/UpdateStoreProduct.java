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

public class UpdateStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;
    static String storeId;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.login(saarUserID, "saar", "fadida");
        impl.login(tomUserID, "tom", "shlaifer");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeId, true, false);
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(tomUserID,storeId,"weddingDress", 11, 4,
                                                            "pink", "clothes").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"heels", 1, 41,
                "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"weddingDress", 11, -4,
                "pink", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom",storeId,false,false);
        Response<String> response = impl.updateProductInStore(tomUserID,storeId,"heels", 14, 46,
                "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.noInventoryPermissions.toString(), response.getDescription());
    }
}
