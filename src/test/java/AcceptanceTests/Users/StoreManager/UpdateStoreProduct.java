package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateStoreProduct {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID, "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        tomUserID = impl.enterMarketSystem().getResult();
        impl.register(tomUserID, "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.login(saarUserID, "saar", "fadida");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreManager(saarUserID, "tom", 0, true, false);
        impl.addProductToStore(saarUserID, 0, new ProductDTO("weddingDress", 10, 5, "pink", "clothes"));
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateProductInStore(tomUserID,0, new ProductDTO("weddingDress", 11, 4,
                                                            "pink", "clothes")).isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.updateProductInStore(tomUserID,0,new ProductDTO("heels", 1, 41,
                "black", "shoes")).isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.updateProductInStore(tomUserID,0,new ProductDTO("weddingDress", 11, -4,
                "pink", "clothes")).isSuccess());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(saarUserID,"tom",0,false,false);
        assertFalse(impl.updateProductInStore(tomUserID,0,new ProductDTO("heels", 14, 46,
                "black", "shoes")).isSuccess());
    }
}
