package AcceptanceTests.Users.StoreManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddStoreProduct {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register(0, "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        impl.enterMarketSystem();
        impl.register(1, "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.login(0, "saar", "fadida");
        impl.login(1, "tom", "shlaifer");
        impl.openStore(0, "alona", "shopping");
        impl.appointStoreManager(0, "tom", 0, true, false);
        impl.addProductToStore(1, 0, "weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulAdditionTest() {
        assertTrue(impl.addProductToStore(1,0,"heels", 4, 2, "black", "shoes").isSuccess());
    }

    @Test
    public void alreadyExistTest() {
        assertFalse(impl.addProductToStore(0,0,"weddingDress", 3, 6, "pink", "clothes").isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.addProductToStore(0,0,"shirt", 5, -4, "green", "clothes").isSuccess());
    }

    @Test
    public void noPermissionTest() {
        impl.updateStoreManagerPermissions(0,"tom",0,false,false);
        assertFalse(impl.addProductToStore(1,0,"heels", 3, 3, "black", "shoes").isSuccess());
    }
}
