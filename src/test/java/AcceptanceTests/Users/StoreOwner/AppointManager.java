package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AppointManager {
    private static BridgeToTests impl;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        impl.enterMarketSystem();
        impl.register("0","saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        impl.register("1","tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        impl.register("2","jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
        impl.register("3","ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "havia");
        impl.login("0", "saar", "fadida");
        impl.openStore("0", "alona", "shopping");
        impl.appointStoreOwner("0", "tom", "0");
        impl.appointStoreManager("0", "jalal", "0", true, false);
    }

    @Test
    public void successfulAppointmentTest() {
        assertTrue(impl.appointStoreManager("0", "ovad",
                "0", true, false).isSuccess());
    }

    @Test
    public void alreadyHasRoleInThisStore() {
        Response<String> response1 = impl.appointStoreManager("0", "tom",
                "0", true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response1.getDescription());

        Response<String> response2 = impl.appointStoreManager("0", "jalal",
                "0", true, false);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response2.getDescription());

    }
}
