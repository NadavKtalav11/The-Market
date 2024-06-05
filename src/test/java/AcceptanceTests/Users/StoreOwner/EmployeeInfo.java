package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class EmployeeInfo {
    private static BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String userID3;
    private static String userID4;
    private static String storeID;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        userID4 = impl.enterMarketSystem().getData();
        impl.register(userID1,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.register(userID2,"tom", "27/11/85", "Israel", "Jerusalem", "Yehuda halevi 17", "tom", "Shlaifer2");
        impl.register(userID3,"jalal", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 13", "jalal", "Kasoomm3");
        impl.register(userID4,"ovad", "08/02/82", "Israel", "Jerusalem", "Yehuda halevi 11", "ovad", "Haviaaa4");
        impl.login(userID1, "saar", "Fadidaa1");
        impl.login(userID2, "tom", "Shlaifer2");
        impl.login(userID3, "jalal", "Kasoomm3");
        impl.login(userID4, "ovad", "Haviaaa4");

        storeID = impl.openStore(userID1, "Zara", "clothing store").getData();
    }

    @Test
    public void successfulRequestTest() {
        impl.appointStoreOwner(userID1, "tom", storeID);
        impl.appointStoreManager(userID1, "jalal", storeID, true, false);

        assertTrue(impl.getInformationAboutRolesInStore(userID1, storeID).isSuccess());
        Map<String, String > employees = new HashMap<>();
        employees.put(userID1, "owner");
        employees.put(userID2, "manager");
        assertEquals(impl.getInformationAboutRolesInStore(userID1, storeID).getResult(), employees);

    }

    @Test
    public void storeNotExistTest() {
        Exception exception = assertThrows(Exception.class, () -> {
            Response<Map<String,String>> response = impl.getInformationAboutRolesInStore(userID1, storeID);
            assertFalse(response.isSuccess());
        });

        assertEquals(ExceptionsEnum.storeNotExist.toString(), exception.getMessage());
    }

}
