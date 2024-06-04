package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String storeID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeID,"weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(saarUserID,storeID,"weddingDress", 11, 4,
                                                            "pink", "clothes").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        Response<String> response = impl.updateProductInStore(saarUserID,storeID,"heels", 1, 41,
                "black", "shoes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.updateProductInStore(saarUserID,storeID,"weddingDress", 11, -4,
                "pink", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }
}
