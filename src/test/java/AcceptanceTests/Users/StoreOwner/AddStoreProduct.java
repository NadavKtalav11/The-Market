package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddStoreProduct {
    private static BridgeToTests impl;
    static String ssarUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        ssarUserID = impl.enterMarketSystem().getResult();
        impl.register(ssarUserID, new UserDTO("saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar"), "fadida");
        impl.login(ssarUserID, "saar", "fadida");
        impl.openStore(ssarUserID, "alona", "shopping");
        impl.addProductToStore(ssarUserID, 0, new ProductDTO("weddingDress", 10, 5, "pink", "clothes"));
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(ssarUserID,0,new ProductDTO("heels", 4, 2, "black", "shoes")).isSuccess());
        assertTrue(impl.addProductToStore(ssarUserID,0,new ProductDTO("skirt", 3, 6, "purple", "clothes")).isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        assertFalse(impl.addProductToStore(ssarUserID,0,new ProductDTO("weddingDress", 3, 6, "pink", "clothes")).isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.addProductToStore(ssarUserID,0,new ProductDTO("shirt", 5, -4, "green", "clothes")).isSuccess());
    }
}
