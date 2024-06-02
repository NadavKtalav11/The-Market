package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UpdateManagerPermissions {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register(0, "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        impl.register(1, "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.register(2, "jalal", "kasoom", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal");
        impl.register(3, "ovad", "havia", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad");
        impl.register(4, "rani", "zelig", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 12", "rani");
        impl.login(0, "saar", "fadida");
        impl.openStore(0, "alona", "shopping");
        impl.appointStoreManager(0, "tom", 0, true, false);
        impl.login(2, "jalal", "kasoom");
        impl.openStore(2, "alona2", "shopping2");
        impl.appointStoreManager(2, "ovad", 1, true, true);
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateStoreManagerPermissions(0, "tom",0,
                true, true).isSuccess());
        assertTrue(impl.updateStoreManagerPermissions(2, "ovad",1,
                true, false).isSuccess());
    }

    @Test
    public void notManagerTest() {
        assertFalse(impl.updateStoreManagerPermissions(0, "rani",0,
                true, false).isSuccess());
    }

    @Test
    public void notNominatorTest() {
        assertFalse(impl.updateStoreManagerPermissions(0, "ovad",0,
                true, false).isSuccess());
        assertFalse(impl.updateStoreManagerPermissions(2, "tom",1,
                true, true).isSuccess());
    }
}
