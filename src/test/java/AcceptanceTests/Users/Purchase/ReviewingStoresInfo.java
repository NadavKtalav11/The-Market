package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewingStoresInfo {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");
        impl.openStore(0, "Bershka", "clothing store");
        impl.openStore(0, "Zara", "clothing store");
        impl.openStore(0, "PullAndBear", "clothing store");
    }

    @Test
    public void successfulViewTest() {
        impl.closeStore(0, 3);

        List<Integer> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(1);
        allAvailableStores.add(2);
        allAvailableStores.add(3);

        assertTrue(impl.getInformationAboutStores(0).isSuccess());
        assertIterableEquals(impl.getInformationAboutStores(0).getResult(), allAvailableStores);
    }
}