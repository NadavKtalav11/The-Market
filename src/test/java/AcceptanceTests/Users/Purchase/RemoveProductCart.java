package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class RemoveProductCart {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() { //
        impl = new ProxyToTest("Real");
        //Do what you need
        impl.enterMarketSystem();
        impl.register("0","user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("0", "user1", "fSijsd281");
        impl.openStore("0", "Zara", "clothing store");
        impl.addProductToStore("0", "0","Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore("0", "0","Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore("0", "0","Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore("0", "0","Shoes", 4, 12, "Nike Shoes", "clothing");

        impl.addProductToBasket("Milk", 2, "0", "0");
        impl.addProductToBasket("Cheese", 4, "0", "0");
        impl.addProductToBasket("Yogurt", 5, "0", "0");
    }
    

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromBasket("Milk", "0", "0").isSuccess());
        assertTrue(impl.removeProductFromBasket("Cheese", "0", "0").isSuccess());
        assertTrue(impl.removeProductFromBasket("Yogurt", "0", "0").isSuccess());
    }

    @Test
    public void productNotExistTest() {

        Response<String> response1 = impl.removeProductFromBasket("Shoes", "0", "0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response1.getDescription());

        Response<String> response2 = impl.removeProductFromBasket("Book", "0", "0");
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response2.getDescription());

        Response<String> response3 = impl.removeProductFromBasket("Juice", "0", "0");
        assertFalse(response3.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response3.getDescription());

    }

    @Test
    public void purchasePolicyInvalidTest() {
        //Assume that in the future 'Shoes' won't meet the purchase policies
        //impl.addProductToBasket("Shoes", 5, 0, 0);
        //assertFalse(impl.removeProductFromBasket("Shoes", 0, 0).isSuccess());
    }
}
