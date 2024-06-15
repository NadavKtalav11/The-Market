package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.PaymentServiceDTO;
import Util.ProductDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PurhcaseHistoryInfo {
    private static BridgeToTests impl;

    static String userId0;
    static String userId1;
    static String storeId;
    static String memberId0;
    static String memberId1;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();


        countries.add("Israel");
        cities.add("Bash");

        userId0 = impl.init(new UserDTO("0" , "manager1", "12/12/12", "Israel", "Bash","Metsada", "Mike"), "imTHeManager1" ,new PaymentServiceDTO( "0","paypal","saddsa.com"),new SupplyServiceDTO("2","DHL", countries, cities)).getData();
        impl.login(userId0, "manager1", "imTHeManager1");
        userId1 = impl.enterMarketSystem().getData();
        memberId1 = impl.register(userId1,"user1", "12/12/00", "Israel", "Beer Sheva", "Mesada", "Toy", "fSijsd281").getData();
        impl.login(userId1, "user1", "fSijsd281");
        storeId= impl.openStore(userId0, "sytro", "sttoe").getData();
        impl.addProductToStore(userId0, storeId,"Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userId0, storeId,"Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userId0, storeId,"Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userId0, storeId,"Shoes", 4, 12, "Nike Shoes", "clothing");
        impl.addProductToBasket("Milk", 2, storeId, userId0);
        //todo
        //impl.(100, "12345", 123, 12, 2000, "389082132", "1");

    }

    @Test
    public void successfulRequestTest() {
        Map<Integer, Integer> receiptIdAndPrice = new HashMap<>();
        receiptIdAndPrice.put(0, 100);
        assertEquals(impl.storeOwnerGetInfoAboutStore(userId0, storeId).getResult(), receiptIdAndPrice);
    }
}
