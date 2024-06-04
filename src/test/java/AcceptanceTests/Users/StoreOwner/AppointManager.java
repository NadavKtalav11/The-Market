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
    static String storeId;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar",  "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom",  "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        ovadUserID = impl.enterMarketSystem().getData();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreOwner(saarUserID, "tom", storeId);
        impl.appointStoreManager(saarUserID, "jalal", storeId, true, false);
    }

    @Test
    public void successfulAppointmentTest() {
        assertTrue(impl.appointStoreManager(saarUserID, "ovad",
                storeId, true, false).isSuccess());
    }

    @Test
    public void alreadyHasRoleInThisStore() {
        Response<String> response1 = impl.appointStoreManager(saarUserID, "tom",
                storeId, true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response1.getDescription());

        Response<String> response2 = impl.appointStoreManager(saarUserID, "jalal",
                storeId, true, false);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.memberAlreadyHasRoleInThisStore.toString(), response2.getDescription());

    }
}
