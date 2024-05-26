package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModifyingCart {
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
        impl.openStore(0,"Grocery", "");
        impl.addProductToStore(0, 0, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(0, 0, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(0, 0, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(0, 0, "Shoes", 4, 12, "Nike Shoes", "clothing");

        impl.addProductToBasket("Milk", 2, 0, 0);
        impl.addProductToBasket("Cheese", 4, 0, 0);
        impl.addProductToBasket("Yogurt", 5, 0, 0);
    }

    @Test
    public void successfulUpdateTest() {
        assertTrue(impl.modifyShoppingCart("Milk", 2,0, 0).isSuccess());
        assertTrue(impl.modifyShoppingCart("Cheese", 3, 0, 0).isSuccess());
        assertTrue(impl.modifyShoppingCart("Yogurt", 4, 0, 0).isSuccess());
    }

    @Test
    public void productNotExistTest() {
        assertFalse(impl.modifyShoppingCart("Tomato", 2,0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("awsdhnjlk", 3, 0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("njknlk", 4, 0, 0).isSuccess());
    }


    @Test
    public void bigQuantityTest() {
        assertFalse(impl.modifyShoppingCart("Milk", 100,0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("Cheese", 200, 0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("Yogurt", 300, 0, 0).isSuccess());
    }

    @Test
    public void negQuantityTest() {
        assertFalse(impl.modifyShoppingCart("Milk", -1,0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("Cheese", -2, 0, 0).isSuccess());
        assertFalse(impl.modifyShoppingCart("Yogurt", -3, 0, 0).isSuccess());
    }

    @Test
    public void purchasePolicyInvalidTest() {
        //Assume that in the future 'Shoes' won't meet the purchase policies
        impl.addProductToBasket("Shoes", 5, 0, 0);
        assertFalse(impl.modifyShoppingCart("Shoes", 2,0, 0).isSuccess());
    }
}
