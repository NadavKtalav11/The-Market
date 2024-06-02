package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AppointOwner {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register("0", "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        impl.register("1", "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.register("2", "jalal", "kasoom", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal");
        impl.login("0", "saar", "fadida");
        impl.openStore("0", "alona", "shopping");
        impl.appointStoreOwner("0", "tom", "0");
    }

    @Test
    public void successfulAppointedTest() {
        assertTrue(impl.appointStoreOwner("0", "jalal","0").isSuccess());
    }

    @Test
    public void alreadyStoreOwnerTest() {
        assertFalse(impl.appointStoreOwner("0", "tom","0").isSuccess());
    }
}
