package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeePermissionsInfo {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register(0, "saar", "fadida", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar");
        impl.register(1, "tom", "shlaifer", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom");
        impl.register(2, "jalal", "kasoom", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal");
        impl.register(3, "ovad", "havia", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad");
        impl.login(0, "saar", "fadida");

    }

    @Test
    public void successfulRequestTest() {
        impl.openStore(0, "Zara", "clothing store");
        impl.appointStoreOwner(0, "tom", 0);
        impl.appointStoreManager(0, "jalal", 0, true, false);
        impl.appointStoreManager(0, "ovad", 0, true, false);

        assertTrue(impl.getAuthorizationsOfManagersInStore(0, 0).isSuccess());
    }

    @Test
    public void storeNotExistTest() {
        assertFalse(impl.getInformationAboutRolesInStore(0, 0).isSuccess());
    }
}