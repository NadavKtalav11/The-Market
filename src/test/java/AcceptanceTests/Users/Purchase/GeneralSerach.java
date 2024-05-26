package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GeneralSerach {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");
//        todo remove before push
//        impl.openStore(0);
//        impl.openStore(0, "Bershka", "clothing store");
//        impl.addProductToStore(0, 0, 0, "Milk", 10, 5, "Milk 5%", "food");
//        impl.addProductToStore(0, 0, 0, "Cheese", 15, 8, "Cheese 22%", "food");
//        impl.addProductToStore(0, 0, 1, "Yogurt", 4, 12, "Yogurt 20%", "food");
//        impl.addProductToStore(0, 0, 1, "Shoes", 4, 12, "Nike Shoes", "clothing");

    }

    @Test
    public void successfulSearchNoFiltersTest() {
        List<String> productNames = new ArrayList<>();
        productNames.add("Milk");
        productNames.add("Cheese");
        productNames.add("Yogurt");
        productNames.add("Shoes");
        assertTrue(impl.generalProductSearch(0, null, null, null).isSuccess());
        assertIterableEquals(impl.generalProductSearch(0, null, null, null).getResult(), productNames);
    }

    @Test
    public void successfulSearchWithFiltersTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertTrue(impl.generalProductSearch(0, null, "FOOD", null).isSuccess());
        assertIterableEquals(impl.generalProductSearch(0, null, "FOOD", null).getResult(), diaryProducts);

        List<String> shoes = new ArrayList<>();
        shoes.add("Shoes");
        assertTrue(impl.generalProductSearch(0, "Shoes", null, null).isSuccess());
        assertIterableEquals(impl.generalProductSearch(0, "Shoes", null, null).getResult(), diaryProducts);
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.generalProductSearch(0, "Tomato", null, null).isSuccess());
        assertFalse(impl.generalProductSearch(0, "Shirt", null, null).isSuccess());
    }

    @Test
    public void categoryNotExistTest() {
        assertFalse(impl.generalProductSearch(0, null, "asdsjd", null).isSuccess());
        assertFalse(impl.generalProductSearch(0, null, "asdsjdasdkdf", null).isSuccess());
    }

    @Test
    public void negativePriceRangeTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(0, null, null, 10, 0, null, diaryProducts, null).isSuccess());
    }

    @Test
    public void productRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(0, null, null, null, null, 7.0, diaryProducts, null).isSuccess());
    }

    @Test
    public void storeRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(0, null, null, null, null, null, diaryProducts, 7.0).isSuccess());
    }
}
