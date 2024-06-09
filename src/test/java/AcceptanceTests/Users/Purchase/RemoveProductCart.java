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
    private static String userID1;
    private static String storeID1;


    @BeforeAll
    public static void setUp() { //
        impl = new ProxyToTest("Real");
        //Do what you need
        userID1 = impl.enterMarketSystem().getData();
        impl.register(userID1,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login(userID1, "user1", "fSijsd281");
        storeID1 = impl.openStore(userID1, "Zara", "clothing store").getData();
        impl.addProductToStore(userID1, storeID1,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userID1, storeID1,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userID1, storeID1,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userID1, storeID1,"Shoes", 4, 12, "Nike Shoes", "clothing");

        impl.addProductToBasket("Milk", 2, storeID1, userID1);
        impl.addProductToBasket("Cheese", 4, storeID1, userID1);
        impl.addProductToBasket("Yogurt", 5, storeID1, userID1);
    }
    

    @Test
    public void successfulRemoveTest() {
        assertTrue(impl.removeProductFromBasket("Milk", storeID1, userID1).isSuccess());
        assertTrue(impl.removeProductFromBasket("Cheese", storeID1, userID1).isSuccess());
        assertTrue(impl.removeProductFromBasket("Yogurt", storeID1, userID1).isSuccess());
    }

    @Test
    public void productNotExistTest() {

        Response<String> response1 = impl.removeProductFromBasket("Shoes", storeID1, userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response1.getDescription());

        Response<String> response2 = impl.removeProductFromBasket("Book", storeID1, userID1);
        assertFalse(response2.isSuccess());
        assertEquals(ExceptionsEnum.productNotExistInCart.toString(), response2.getDescription());

        Response<String> response3 = impl.removeProductFromBasket("Juice", storeID1, userID1);
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
