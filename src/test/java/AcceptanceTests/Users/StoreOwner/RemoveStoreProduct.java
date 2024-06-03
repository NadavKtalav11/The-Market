package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RemoveStoreProduct {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register("0","saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.login("0", "saar", "fadida");
        impl.openStore("0", "alona", "shopping");
        impl.addProductToStore("0", "0","weddingDress", 10, 5, "pink", "clothes");
    }

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromStore("0", "0","weddingDress").isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.removeProductFromStore("0", "0","skirt").isSuccess());
    }
}
