package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurhcaseHistoryInfo {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");
        impl.init(new UserDTO("manager1", "12/12/12", "Israel", "Bash","Metsada", "Mike"), "imTHeManager" , 0,"paypal","saddsa.com",2,"DHL", countries, cities);
        impl.enterMarketSystem();
        impl.register("1", new UserDTO("user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy"), "fSijsd281");
        impl.login("1", "user1", "fSijsd281");
        impl.openStore("0", "sytro", "sttoe");
        impl.addProductToStore("0", "0", new ProductDTO("Milk", 10, 5, "Milk 5%", "food"));
        impl.addProductToStore("0", "0", new ProductDTO("Cheese", 15, 8, "Cheese 22%", "food"));
        impl.addProductToStore("0", "0", new ProductDTO("Yogurt", 4, 12, "Yogurt 20%", "food"));
        impl.addProductToStore("0", "0", new ProductDTO("Shoes", 4, 12, "Nike Shoes", "clothing"));
        impl.addProductToBasket("Milk", 2, "0", "0");
        //todo
        //impl.payWithExternalPaymentService(100, "12345", 123, 12, 2000, "389082132", "1");

    }

    @Test
    public void successfulRequestTest() {
        Map<Integer, Integer> receiptIdAndPrice = new HashMap<>();
        receiptIdAndPrice.put(0, 100);
        assertEquals(impl.storeOwnerGetInfoAboutStore("0", "0").getResult(), receiptIdAndPrice);
    }
}
