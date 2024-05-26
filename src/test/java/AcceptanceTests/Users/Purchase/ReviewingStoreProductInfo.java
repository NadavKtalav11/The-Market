package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ReviewingStoreProductInfo {
    private static BridgeToTests impl;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");

    }

    @Test
    public void successfulViewTest() {
        impl.openStore(0, "Bershka", "clothing store");
        impl.addProductToStore(0, 0, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(0, 0, "Cheese", 15, 8, "Cheese 22%", "food");

        List<String> storeProducts = new ArrayList<>();
        storeProducts.add("Milk");
        storeProducts.add("Cheese");

        assertTrue(impl.getInformationAboutProductInStore(0, 0).isSuccess());
        assertIterableEquals(impl.getInformationAboutProductInStore(0, 0).getResult(), storeProducts);
    }

    @Test
    public void storeNotExistTest() {
        assertFalse(impl.getInformationAboutProductInStore(0, 0).isSuccess());}
}
