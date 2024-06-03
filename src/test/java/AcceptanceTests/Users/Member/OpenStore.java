package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OpenStore {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register("0", "user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("0", "user1", "fSijsd281");

    }

    @Test
    public void successfulOpenStoreTest() {
        assertTrue(impl.openStore("0", "Bershka", "clothing store").isSuccess());
        assertTrue(impl.openStore("0", "Zara", "clothing store").isSuccess());
        assertTrue(impl.openStore("0", "shufersal", "Food store").isSuccess());
    }

    @Test
    public void missingStoreNameTest() {
        Exception exception1 = assertThrows(Exception.class, () -> {
            Response<String> response = impl.openStore("0", null, "clothing store");
            assertFalse(response.isSuccess());
        });

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), exception1.getMessage());

        Exception exception2 = assertThrows(Exception.class, () -> {
            Response<String> response = impl.openStore("0", "", "Electronics store");
            assertFalse(response.isSuccess());
        });

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), exception2.getMessage());
    }
}
