package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.UserDTO;
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
        impl.register("0", new UserDTO("user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy"), "fSijsd281");
        impl.login("0", "user1", "fSijsd281");
        impl.openStore("0", "Bershka", "clothing store");
        impl.openStore("0", "Zara", "clothing store");
        impl.openStore("0", "PullAndBear", "clothing store");
    }

    @Test
    public void successfulViewTest() {
        impl.closeStore("0", "3");

        List<Integer> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(0);
        allAvailableStores.add(1);
        allAvailableStores.add(2);

        Response<List<String>> res = impl.getInformationAboutStores("0");
        assertTrue(res.isSuccess());
        assertIterableEquals(res.getResult(), allAvailableStores);
    }
}
