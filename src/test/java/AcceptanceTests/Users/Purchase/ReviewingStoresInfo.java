package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.PaymentDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ReviewingStoresInfo {
    private static BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String userID3;
    private static String storeID1;
    private static String storeID2;
    private static String storeID3;

    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        userID3 = impl.enterMarketSystem().getData();
        impl.register(userID1,"user1",  "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.register(userID2,"user2",  "12/12/99", "Israel", "Beer Sheva", "Mesada", "Nitzan", "fSijsd28cd1");

        impl.login(userID1, "user1", "fSijsd281");
        impl.login(userID2, "user2", "fSijsd28cd1");

        storeID1 = impl.openStore(userID1, "Bershka", "clothing store").getData();
        storeID2 = impl.openStore(userID1, "Zara", "clothing store").getData();
        storeID3 = impl.openStore(userID1, "PullAndBear", "clothing store").getData();
    }

    @Test
    public void successfulStoreOwnerViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);
        allAvailableStores.add(storeID3);

        Response<List<String>> res = impl.getInformationAboutStores(userID1);
        assertTrue(res.isSuccess());
        assertIterableEquals(res.getResult(), allAvailableStores);
    }

    @Test
    public void successfulMemberViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);

        Response<List<String>> res = impl.getInformationAboutStores(userID2);
        assertTrue(res.isSuccess());
        assertIterableEquals(res.getResult(), allAvailableStores);
    }

    @Test
    public void successfulUserViewTest() {
        impl.closeStore(userID1, storeID3);

        List<String> allAvailableStores = new ArrayList<>();
        allAvailableStores.add(storeID1);
        allAvailableStores.add(storeID2);

        Response<List<String>> res = impl.getInformationAboutStores(userID3);
        assertTrue(res.isSuccess());
        assertIterableEquals(res.getResult(), allAvailableStores);
    }
}
