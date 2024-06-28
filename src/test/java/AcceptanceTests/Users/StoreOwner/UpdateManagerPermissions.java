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
    static String storeIDsaar;
    static String storeIDjalal;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        tomUserID = impl.enterMarketSystem().getData();
        impl.register(tomUserID,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        jalalUserID = impl.enterMarketSystem().getData();
        impl.register(jalalUserID,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        ovadUserID = impl.enterMarketSystem().getData();
        impl.register(ovadUserID,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4");
        raniUserID = impl.enterMarketSystem().getData();
        impl.register(raniUserID,"rani", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 12", "rani", "Zeliggg5");

        impl.login(saarUserID, "saar", "Fadidaa1");
        storeIDsaar = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.appointStoreManager(saarUserID, "tom", storeIDsaar, true, false);
        impl.login(jalalUserID, "jalal", "Kasoomm3");
        storeIDjalal = impl.openStore(jalalUserID, "alona2", "shopping2").getData();
        impl.appointStoreManager(jalalUserID, "ovad", storeIDjalal, true, true);
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.updateStoreManagerPermissions(saarUserID, "tom",storeIDsaar,
                true, true).isSuccess());
        assertTrue(impl.updateStoreManagerPermissions(jalalUserID, "ovad",storeIDjalal,
                true, false).isSuccess());
    }

    @Test
    public void notManagerTest() {
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "rani",storeIDsaar,
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notManager.toString(), response1.getDescription());
    }

    @Test
    public void notNominatorTest() {
        impl.appointStoreOwner(jalalUserID, "saar",storeIDjalal);
        Response<String> response1 = impl.updateStoreManagerPermissions(saarUserID, "ovad",storeIDjalal,
                true, false);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisEmployee.toString(), response1.getDescription());

        impl.appointStoreOwner(saarUserID, "jalal", storeIDsaar);
        Response<String> response2 = impl.updateStoreManagerPermissions(jalalUserID ,"tom",storeIDsaar,
                true, true);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.notNominatorOfThisEmployee.toString(), response2.getDescription());

    }
}
