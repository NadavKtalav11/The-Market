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

public class AddStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String storeId;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.login(saarUserID, "saar", "fadida");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(saarUserID,storeId,"heels", 4, 2, "black", "shoes").isSuccess());
        assertTrue(impl.addProductToStore(saarUserID,storeId,"skirt", 3, 6, "purple", "clothes").isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        Response<String> response = impl.addProductToStore(saarUserID,storeId,"weddingDress", 3, 6, "pink", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productAlreadyExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.addProductToStore(saarUserID,storeId,"shirt", 5, -4, "green", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }
}
