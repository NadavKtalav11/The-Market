package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppointOwner {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register("0","saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.register("1","tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.register("2","jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
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
        Response<String> response = impl.appointStoreOwner("0", "tom","0");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
    }
}
