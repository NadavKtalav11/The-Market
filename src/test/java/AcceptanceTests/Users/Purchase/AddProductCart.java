package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class AddProductCart {

    private static BridgeToTests impl;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register(0, "user1", "fSijsd281", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy");
        impl.login(0, "user1", "fSijsd281");
        impl.openStore(0, "big", "very good store");
        impl.addProductToStore(0, 0, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(0, 0, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(0, 0, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(0, 0, "Shoes", 4, 12, "Nike Shoes", "clothing");
    }

    @AfterEach
    public void shutDown()
    {
        impl.exitMarketSystem(0);
    }

    @Test
    public void successfulAdditionTest()
    {
        assertTrue(impl.addProductToBasket("Milk", 2, 0, 0).isSuccess());
        assertTrue(impl.addProductToBasket("Cheese", 4, 0, 0).isSuccess());
        assertTrue(impl.addProductToBasket("Yogurt", 5, 0, 0).isSuccess());
    }

    @Test
    public void invalidProductNameTest()
    {
        assertFalse(impl.addProductToBasket("Shoes", 2, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Shirt", 4, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("TV", 5, 0, 0).isSuccess());
    }

    @Test
    public void outOfStockProductTest()
    {
        impl.addProductToBasket("Milk", 5, 0, 0);
        impl.addProductToBasket("Cheese", 8, 0, 0);
        impl.addProductToBasket("Yogurt", 12, 0, 0);
        assertFalse(impl.addProductToBasket("Milk", 1, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Cheese", 2, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Yogurt", 3, 0, 0).isSuccess());
    }

    @Test
    public void bigQuantityTest()
    {
        assertFalse(impl.addProductToBasket("Milk", 10, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Cheese", 100, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Yogurt", 90, 0, 0).isSuccess());
    }

    @Test
    public void negQuantityTest()
    {
        assertFalse(impl.addProductToBasket("Milk", -4, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Cheese", -1, 0, 0).isSuccess());
        assertFalse(impl.addProductToBasket("Yogurt", -8, 0, 0).isSuccess());
    }

    @Test
    public void purchasePolicyInvalidTest()
    {
        //Assume that in the future 'Shoes' won't meet the purchase policies
        assertFalse(impl.addProductToBasket("Shoes", 1, 0, 0).isSuccess());
    }
}
