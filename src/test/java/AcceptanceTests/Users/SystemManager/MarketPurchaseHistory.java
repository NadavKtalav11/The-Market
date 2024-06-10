package AcceptanceTests.Users.SystemManager;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MarketPurchaseHistory {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");

        impl.init(new UserDTO("-1", "manager1" ,"12/12/12", "Israel", "Bash", "Metsada", "Mike"), "imTHeManager1",new PaymentServiceDTO("0","paypal","saddsa.com"),
                new SupplyServiceDTO("2","DHL", countries, cities));

        impl.enterMarketSystem();
        impl.register("1","user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281");
        impl.login("1", "user1", "fSijsd281");
        impl.openStore("0", "store", "good");
        impl.addProductToStore("0", "0","Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore("0", "0","Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore("0", "0","Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore("0", "0","Shoes", 4, 12, "Nike Shoes", "clothing");
        impl.addProductToBasket("Milk", 2, "0", "0");
        //todo think if we need
        //impl.payWithExternalPaymentService(100, "12345", 123, 12, 2000, "389082132", "1");
    }

    @Test
    public void successfulRequestTest() {
        Map<Integer, Integer> storeIdAndNumOfAcquistion = new HashMap<>();
        storeIdAndNumOfAcquistion.put(0, 1);
        assertEquals(impl.marketManagerAskInfo("0").getResult(), storeIdAndNumOfAcquistion);
    }

    @Test
    public void noPermissionsTest() {
        Response<Map<String, Integer>> response1 = impl.marketManagerAskInfo("0");
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notSystemManager.toString(), response1.getDescription());
    }
}
