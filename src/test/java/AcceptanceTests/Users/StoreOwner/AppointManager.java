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
    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String ovadUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID,"saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getResult();
        impl.register(tomUserID,"tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        jalalUserID = impl.enterMarketSystem().getResult();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
        ovadUserID = impl.enterMarketSystem().getResult();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "havia");
        impl.login(saarUserID, "saar", "fadida");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreOwner(saarUserID, "tom", "0");
        impl.appointStoreManager(saarUserID, "jalal", "0", true, false);
    }

    @Test
    public void successfulAppointmentTest() {
        assertTrue(impl.appointStoreManager(saarUserID, "ovad",
                "0", true, false).isSuccess());
    }

    @Test
    public void alreadyHasRoleInThisStore() {
        Response<String> response1 = impl.appointStoreManager(saarUserID, "tom",
                "0", true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response1.getDescription());

        Response<String> response2 = impl.appointStoreManager(saarUserID, "jalal",
                "0", true, false);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response2.getDescription());

    }
}
