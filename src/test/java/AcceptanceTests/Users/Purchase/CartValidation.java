package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartValidation {

    private static BridgeToTests impl;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        HashSet<String> countries = new HashSet<>();
        countries.add("Israel");
        HashSet<String> cities = new HashSet<>();
        cities.add("Beer Sheva");
        impl.enterMarketSystem();
        impl.init("KobiM", "123", "27/4/95", "Israel", "Beer Sheva", "Mesada", "kobi Menashe", 1, "payementService", "kobi@gmail.com", 2, "supplyService", countries, cities);
        impl.enterMarketSystem();
        impl.register(1, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(1, "user1", "fSijsd281");
        impl.openStore(1, "super-li", "food store");
        impl.addProductToStore(1, 0, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(1, 0, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(1, 0, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(1, 0, "Shoes", 4, 12, "Nike Shoes", "clothing");
        impl.enterMarketSystem();
        impl.addProductToBasket("Milk", 2, 0, 2);
        impl.addProductToBasket("Cheese", 4, 0, 2);
        impl.addProductToBasket("Yogurt", 5, 0, 2);

    }

    @Test
    public void successfulCheckingTest() {
        assertTrue(impl.checkingCartValidationBeforePurchase(2, "Israel", "Beer Sheva", "Mesada").isSuccess());
        assertEquals(impl.checkingCartValidationBeforePurchase(2, "Israel", "Beer Sheva", "Mesada").getResult(), 100);

    }

    @Test
    public void productQuantityUnavailableTest() {
        impl.addProductToBasket("Shoes", 13, 0, 1);
        assertFalse(impl.checkingCartValidationBeforePurchase(0, "Israel", "Beer Sheva", "Mesada").isSuccess());

    }

    @Test
    public void productNotExistTest() {
        impl.addProductToBasket("pants", 5, 0, 1);
        assertFalse(impl.checkingCartValidationBeforePurchase(0, "Israel", "Beer Sheva", "Mesada").isSuccess());
    }

    @Test
    public void emptyCartTest() {
        impl.removeProductFromBasket("Milk", 0, 1);
        impl.removeProductFromBasket("Cheese", 0, 1);
        impl.removeProductFromBasket("Yogurt", 0, 1);
        assertFalse(impl.checkingCartValidationBeforePurchase(0, "Israel", "Beer Sheva", "Mesada").isSuccess());
    }

    @Test
    public void purchasePolicyInvalidTest() {
        assertFalse(impl.checkingCartValidationBeforePurchase(0, "Israel", "Beer Sheva", "Mesada").isSuccess());
    }

    @Test
    public void shippingInvalidTest() {
        assertFalse(impl.checkingCartValidationBeforePurchase(0, "Israel", "Tel Aviv", "Mesada").isSuccess());
    }

}
