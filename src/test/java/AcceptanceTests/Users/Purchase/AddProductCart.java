package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
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
        impl.register("0", "user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("0", "user1", "fSijsd281");

        impl.openStore("0", "Zara", "clothing store");
        impl.addProductToStore("0", "0", "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore("0", "0", "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore("0", "0", "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore("0", "0", "Shoes", 4, 12, "Nike Shoes", "clothing");
    }


    @Test
    public void successfulAdditionTest()
    {
        assertTrue(impl.addProductToBasket("Milk", 2, "0", "0").isSuccess());
        assertTrue(impl.addProductToBasket("Cheese", 4, "0", "0").isSuccess());
        assertTrue(impl.addProductToBasket("Yogurt", 5, "0", "0").isSuccess());
    }

    @Test
    public void invalidProductNameTest()
    {

        Response<String> response1 = impl.addProductToBasket("Computer", 2, "0", "0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Shirt", 4, "0", "0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("TV", 5, "0", "0");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response3.getDescription());
    }

    @Test
    public void outOfStockProductTest()
    {
        impl.addProductToStore("0", "0", "Mouse", 10, 0, "HP Mouse", "electronics");
        impl.addProductToStore("0", "0", "Laptop", 15, 0, "HP Laptop ", "electronics");


        Response<String> response1 = impl.addProductToBasket("Mouse", 1, "0", "0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Laptop", 2, "0", "0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());
    }

    @Test
    public void bigQuantityTest()
    {

        Response<String> response1 = impl.addProductToBasket("Milk", 10, "0", "0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Cheese", 9, "0", "0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("Yogurt", 13, "0", "0");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response3.getDescription());
    }

    @Test
    public void negQuantityTest()
    {

        Response<String> response1 = impl.addProductToBasket("Milk", -4, "0", "0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response1.getDescription());


        Response<String> response2 = impl.addProductToBasket("Cheese", -1, "0", "0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response2.getDescription());


        Response<String> response3 = impl.addProductToBasket("Yogurt", -8, "0", "0");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), response3.getDescription());
    }

    @Test
    public void purchasePolicyInvalidTest()
    {
        //Test will fil, no purchase policies yet
        //assertFalse(impl.addProductToBasket("Shoes", 1, 0, 0).isSuccess());
    }
}
