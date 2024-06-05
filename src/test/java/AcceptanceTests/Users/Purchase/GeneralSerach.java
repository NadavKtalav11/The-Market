package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class GeneralSerach {
    private static BridgeToTests impl;
    static String userId0;
    static String storeId0;
    static String storeId1;
    


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl = new ProxyToTest("Real");
        //Do what you need
        userId0= impl.enterMarketSystem().getData();
        impl.register(userId0,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userId0, "user1", "fSijsd281");
        storeId0= impl.openStore(userId0, "Zara", "clothing store").getData();
        storeId1 = impl.openStore(userId0, "Bershka", "clothing store").getData();
        impl.addProductToStore(userId0, storeId0,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userId0, storeId0,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userId0, storeId1,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userId0, storeId1,"Shoes", 4, 12, "Nike Shoes", "clothing");

    }

    @Test
    public void successfulSearchNoFiltersTest() {
        List<String> productNames = new ArrayList<>();
        productNames.add("Milk");
        productNames.add("Cheese");
        productNames.add("Yogurt");
        productNames.add("Shoes");
        Set<String> productsSet = new HashSet<String>(productNames);
        Response<List<String>> res = impl.generalProductSearch(userId0, null, null, null);
        assertTrue(res.isSuccess());
        List<String> unFilteredProducts = res.getResult();
        Set<String> filteredProductsSet = new HashSet<String>(unFilteredProducts);
        assertIterableEquals(filteredProductsSet, productsSet);
    }

    @Test
    public void successfulSearchWithFiltersTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");

        Set<String> dairySet = new HashSet<String>(diaryProducts);

        assertTrue(impl.generalProductSearch(userId0, null, "FOOD", null).isSuccess());
        List<String> filteredProducts = impl.generalProductSearch(storeId0, null, "FOOD", null).getResult();
        Set<String> filteredProductsSet = new HashSet<String>(filteredProducts);
        assertIterableEquals(filteredProductsSet, dairySet);

        List<String> shoes = new ArrayList<>();
        shoes.add("Shoes");
        assertTrue(impl.generalProductSearch(userId0, "Shoes", null, null).isSuccess());
        assertIterableEquals(impl.generalProductSearch(userId0, "Shoes", null, null).getResult(), shoes);
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.generalProductSearch(userId0, "Tomato", null, null).isSuccess());
        assertFalse(impl.generalProductSearch(userId0, "Shirt", null, null).isSuccess());
    }

    @Test
    public void categoryNotExistTest() {
        assertFalse(impl.generalProductSearch(userId0, null, "asdsjd", null).isSuccess());
        assertFalse(impl.generalProductSearch(userId0, null, "asdsjdasdkdf", null).isSuccess());
    }

    @Test
    public void negativePriceRangeTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(userId0, null, null, 10, 0, null, diaryProducts, null).isSuccess());
    }

    @Test
    public void productRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(userId0, null, null, null, null, 7.0, diaryProducts, null).isSuccess());
    }

    @Test
    public void storeRatingInvalidTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.generalProductFilter(userId0, null, null, null, null, null, diaryProducts, 7.0).isSuccess());
    }
}
