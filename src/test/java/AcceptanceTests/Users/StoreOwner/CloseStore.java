package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloseStore {
    private static BridgeToTests impl;
    private static String userID1;
    private static String storeID1;
    private static String storeID2;

    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        userID1 = impl.enterMarketSystem().getData();
        impl.register(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa", "Password123");
        impl.login(userID1, "newUser1", "Password123");
        storeID1 = impl.openStore(userID1, "Bershka", "clothing store").getData();
        storeID2 = impl.openStore(userID1, "Zara", "clothing store").getData();

    }

    @Test
    public void successfulCloseTest() {
        assertTrue(impl.closeStore(userID1, storeID1).isSuccess());
        assertTrue(impl.closeStore(userID1, storeID2).isSuccess());
    }

    @Test
    public void alreadyClosedTest() {
        impl.closeStore(userID1, storeID1);

        Response<String> response = impl.closeStore(userID1, storeID1);;
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.storeNotExist.toString(), response.getDescription());
    }

}
