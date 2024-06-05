package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CloseStore {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register("0","user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("0", "user1", "fSijsd281");
        impl.openStore("0", "Bershka", "clothing store");
        impl.openStore("0", "Zara", "clothing store");

    }

    @Test
    public void successfulCloseTest() {
        assertTrue(impl.closeStore("0", "0").isSuccess());
        assertTrue(impl.closeStore("0", "1").isSuccess());
    }

    @Test
    public void alreadyClosedTest() {
        impl.closeStore("0", "0");

        Response<String> response = impl.closeStore("0", "0");;
        assertFalse(response.isSuccess());
        assertEquals(ExceptionsEnum.storeNotExist.toString(), response.getDescription());
    }

}
