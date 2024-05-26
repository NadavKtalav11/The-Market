package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CloseStore {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");
        impl.openStore(0, "Bershka", "clothing store");
        impl.openStore(0, "Zara", "clothing store");

    }

    @Test
    public void successfulCloseTest() {
        assertTrue(impl.closeStore(0, 0).isSuccess());
        assertTrue(impl.closeStore(0, 1).isSuccess());
    }

    @Test
    public void alreadyClosedTest() {
        impl.closeStore(0, 0);
        assertFalse(impl.closeStore(0, 0).isSuccess());
    }

}
