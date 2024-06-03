package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateManagerPermissions {
    private static BridgeToTests impl;
    static String saarUserID;
    static String tomUserID;
    static String jalalUserID;
    static String ovadUserID;
    static String raniUserID;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getResult();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "fadida");
        tomUserID = impl.enterMarketSystem().getResult();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "shlaifer");
        jalalUserID = impl.enterMarketSystem().getResult();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "kasoom");
        ovadUserID = impl.enterMarketSystem().getResult();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "havia");
        raniUserID = impl.enterMarketSystem().getResult();
        impl.register(raniUserID,"rani", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 12", "rani", "zelig");
        impl.login(saarUserID, "saar", "fadida");
        impl.openStore(saarUserID, "alona", "shopping");
        impl.appointStoreManager(saarUserID, "tom", "0", true, false);
        impl.login("2", "jalal", "kasoom");
        impl.openStore("2", "alona2", "shopping2");
        impl.appointStoreManager("2", "ovad", "1", true, true);
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateStoreManagerPermissions(saarUserID, "tom","0",
                true, true).isSuccess());
        assertTrue(impl.updateStoreManagerPermissions(jalalUserID, "ovad","1",
                true, false).isSuccess());
    }

    @Test
    public void notManagerTest() {
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "rani","0",
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notManager.toString(), response1.getDescription());
    }

    @Test
    public void notNominatorTest() {
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "ovad","0",
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisManager.toString(), response1.getDescription());

        Response<String> response2 = impl.updateStoreManagerPermissions(jalalUserID ,"tom","1",
                true, true);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisManager.toString(), response2.getDescription());

    }
}
