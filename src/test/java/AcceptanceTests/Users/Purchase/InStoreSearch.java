package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class InStoreSearch {
    private static BridgeToTests impl;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

        impl.enterMarketSystem();
        impl.register("0","user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("0", "user1", "fSijsd281");
        impl.openStore("0", "Bershka", "clothing store");
        impl.addProductToStore("0", "0","Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore("0", "0","Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore("0", "0","Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore("0", "0","Shoes", 4, 12, "Nike Shoes", "clothing");
    }

    @Test
    public void successfulSearchNoFiltersTest() {
        List<String> productNames = new ArrayList<>();
        productNames.add("Milk");
        productNames.add("Cheese");
        productNames.add("Yogurt");
        productNames.add("Shoes");
        Set<String> productsSet = new HashSet<String>(productNames);
        Response<List<String>> res = impl.inStoreProductSearch("0", null, null, null, "0");
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

        assertTrue(impl.inStoreProductSearch("0", null, "FOOD", null, "0").isSuccess());
        List<String> filteredProducts = impl.inStoreProductSearch("0", null, "FOOD", null, "0").getResult();
        Set<String> filteredProductsSet = new HashSet<String>(filteredProducts);
        assertIterableEquals(filteredProductsSet, dairySet);

        List<String> shoes = new ArrayList<>();
        shoes.add("Shoes");
        assertTrue(impl.inStoreProductSearch("0", "Shoes", null, null, "0").isSuccess());
        assertIterableEquals(impl.inStoreProductSearch("0", "Shoes", null, null, "0").getResult(), shoes);
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.inStoreProductSearch("0", "Tomato", null, null, "0").isSuccess());
        assertFalse(impl.inStoreProductSearch("0", "Shirt", null, null, "0").isSuccess());
    }

    @Test
    public void categoryNotExistTest() {
        assertFalse(impl.inStoreProductSearch("0", null, "asdsjd", null, "0").isSuccess());
        assertFalse(impl.inStoreProductSearch("0", null, "asdsjdasdkdf", null, "0").isSuccess());
    }

    @Test
    public void negativePriceRangeTest() {
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.inStoreProductFilter("0", null, null, 10, 0, null, "0", diaryProducts, null).isSuccess());
    }

    @Test
    public void productRatingInvalidTest() { //
        List<String> diaryProducts = new ArrayList<>();
        diaryProducts.add("Milk");
        diaryProducts.add("Cheese");
        diaryProducts.add("Yogurt");
        assertFalse(impl.inStoreProductFilter("0", null, null, null, null, 7.0, "0", diaryProducts, null).isSuccess());
    }

}
