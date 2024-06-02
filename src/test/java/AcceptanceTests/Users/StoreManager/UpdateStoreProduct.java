package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStoreProduct {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register("0", "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        impl.register("1", "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.login("0", "saar", "fadida");
        impl.openStore("0", "alona", "shopping");
        impl.appointStoreManager("0", "tom", "0", true, false);
        impl.addProductToStore("0", "0", "weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore("1","0","weddingDress", 11, 4,
                                                            "pink", "clothes").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.updateProductInStore("1","0","heels", 1, 41,
                "black", "shoes").isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.updateProductInStore("1","0","weddingDress", 11, -4,
                "pink", "clothes").isSuccess());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions("0","tom","0",false,false);
        assertFalse(impl.updateProductInStore("1","0","heels", 14, 46,
                "black", "shoes").isSuccess());
    }
}
