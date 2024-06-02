package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OpenStore {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");

    }

    @Test
    public void successfulOpenStoreTest() {
        assertTrue(impl.openStore(0, "Bershka", "clothing store").isSuccess());
        assertTrue(impl.openStore(0, "Zara", "clothing store").isSuccess());
        assertTrue(impl.openStore(0, "shufersal", "Food store").isSuccess());
    }

    @Test
    public void missingDetailsTest() {
        assertFalse(impl.openStore(0, null, "clothing store").isSuccess());
        assertFalse(impl.openStore(0, null, "Food store").isSuccess());
        assertFalse(impl.openStore(0, "", "Electronics store").isSuccess());
    }
}
