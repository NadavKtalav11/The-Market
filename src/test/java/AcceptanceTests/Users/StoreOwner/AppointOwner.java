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

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getResult();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        jalalUserID = impl.enterMarketSystem().getResult();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
        impl.login(saarUserID, "saar", "fadida");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreOwner(saarUserID, "tom", "0");
    }

    @Test
    public void successfulAppointedTest() {
        assertTrue(impl.appointStoreOwner(saarUserID, "jalal","0").isSuccess());
    }

    @Test
    public void alreadyStoreOwnerTest() {
        Response<String> response = impl.appointStoreOwner(saarUserID, "tom","0");
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.memberIsAlreadyStoreOwner.toString(), response.getDescription());
    }
}
