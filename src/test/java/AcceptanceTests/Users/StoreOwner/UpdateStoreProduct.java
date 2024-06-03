package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStoreProduct {
    private static BridgeToTests impl;
    static String ssarUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        ssarUserID = impl.enterMarketSystem().getResult();
        impl.register(ssarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.login(ssarUserID, "saar", "fadida");
        impl.openStore(ssarUserID, "alona", "shopping");
        impl.addProductToStore(ssarUserID, "0","weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(ssarUserID,"0","weddingDress", 11, 4,
                                                            "pink", "clothes").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.updateProductInStore(ssarUserID,"0","heels", 1, 41,
                "black", "shoes").isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.updateProductInStore(ssarUserID,"0","weddingDress", 11, -4,
                "pink", "clothes").isSuccess());
    }
}
