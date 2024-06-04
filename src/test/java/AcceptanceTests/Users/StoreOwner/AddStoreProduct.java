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

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.addProductToStore(saarUserID, "0","weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(saarUserID,"0","heels", 4, 2, "black", "shoes").isSuccess());
        assertTrue(impl.addProductToStore(saarUserID,"0","skirt", 3, 6, "purple", "clothes").isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        Response<String> response = impl.addProductToStore(saarUserID,"0","weddingDress", 3, 6, "pink", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productAlreadyExistInStore.toString(), response.getDescription());
    }

    @Test
    public void negQuantityTest() {
        Response<String> response = impl.addProductToStore(saarUserID,"0","shirt", 5, -4, "green", "clothes");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response.getDescription());
    }
}
