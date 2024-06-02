package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeePermissionsInfo {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register(0, new UserDTO("saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar"), "fadida");
        impl.register(1, new UserDTO("tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom"), "shlaifer");
        impl.register(2, new UserDTO("jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal"), "kasoom");
        impl.register(3, new UserDTO("ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad"), "havia");
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
        Exception exception = assertThrows(Exception.class, () -> {
            Response<Map<Integer, List<Integer>>> response = impl.getAuthorizationsOfManagersInStore(0, 0);
            assertFalse(response.isSuccess());
        });

        assertEquals(ExceptionsEnum.storeNotExist.toString(), exception.getMessage());
    }
}
