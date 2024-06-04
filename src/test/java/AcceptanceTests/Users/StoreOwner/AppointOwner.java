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
    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String storeID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
        impl.login(saarUserID, "saar", "fadida");
        storeID = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreOwner(saarUserID, "tom", storeID);
    }

    @Test
    public void successfulAppointedTest() {
        assertTrue(impl.appointStoreOwner(saarUserID, "jalal",storeID).isSuccess());
    }

    @Test
    public void alreadyStoreOwnerTest() {
        Response<String> response = impl.appointStoreOwner(saarUserID, "tom",storeID);
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
    }
}
