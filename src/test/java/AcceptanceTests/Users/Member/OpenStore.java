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
    static  String userId0;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        userId0 = impl.enterMarketSystem().getData();
        impl.register(userId0, "user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId0, "user1", "fSijsd281");

    }

    @Test
    public void successfulOpenStoreTest() {
        assertTrue(impl.openStore(userId0, "Bershka", "clothing store").isSuccess());
        assertTrue(impl.openStore(userId0, "Zara", "clothing store").isSuccess());
        assertTrue(impl.openStore(userId0, "shufersal", "Food store").isSuccess());
    }

    @Test
    public void missingStoreNameTest() {
        Response<String> response = impl.openStore(userId0, null, "clothing store");
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), response.getDescription());

        Response<String> response2 = impl.openStore(userId0, "", "Electronics store");
        assertFalse(response.isSuccess());

        assertEquals(ExceptionsEnum.illegalStoreName.toString(), response2.getDescription());
    }
}
