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
    private static String userID1;
    private static String userID2;
    private static String managerID1;
    private static String storeID;
    private static PaymentDTO paymentDTO;
    private static UserDTO userDTO;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need
        HashSet<String> countries = new HashSet<>();
        countries.add("Israel");
        HashSet<String> cities = new HashSet<>();
        cities.add("BeerSheva");
        managerID1 = impl.init(new UserDTO("0", "KobiM", "27/4/95", "Israel","Beer Sheva","Mesada","kobi Menashe"), "Password123",

                new PaymentServiceDTO("1", "payementService", "kobi@gmail.com"),new SupplyServiceDTO("2", "supplyService", countries, cities)).getData();
        impl.login(managerID1, "KobiM", "Password123");


        userID1 = impl.enterMarketSystem().getData();
        userID2 = impl.enterMarketSystem().getData();
        impl.register(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa", "Password123");
        impl.login(userID1, "newUser1", "Password123");

        storeID = impl.openStore(userID1, "superStore", "supermarket").getData();
        impl.addProductToStore(userID1, storeID, "Milk", 10, 5, "Milk 5%", "food");
        impl.addProductToStore(userID1, storeID, "Cheese", 15, 8, "Cheese 22%", "food");
        impl.addProductToStore(userID1, storeID, "Yogurt", 4, 12, "Yogurt 20%", "food");
        impl.addProductToStore(userID1, storeID, "Shoes", 4, 12, "Nike Shoes", "clothing");
        impl.enterMarketSystem();
        impl.addProductToBasket("Milk", 2, storeID, userID2);
        impl.addProductToBasket("Cheese", 4, storeID, userID2);
        impl.addProductToBasket("Yogurt", 5, storeID, userID2);

        // Initialize paymentDTO and userDTO
        paymentDTO = new PaymentDTO("holderName", "1111222233334444", 1, 12, 2025);
        userDTO = new UserDTO(userID1, "newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa");
        impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId()).isSuccess();
    }

    @Test
    public void successfulRequestTest() {
        Map<String, Integer> storeIdAndNumOfAcquistion = new HashMap<>();
        storeIdAndNumOfAcquistion.put(storeID, 1);
        assertEquals(impl.marketManagerAskInfo(managerID1).getResult(), storeIdAndNumOfAcquistion);
    }

    @Test
    public void noPermissionsTest() {
        Response<Map<String, Integer>> response1 = impl.marketManagerAskInfo(userID1);
        assertFalse(response1.isSuccess());
        assertEquals(ExceptionsEnum.notSystemManager.toString(), response1.getDescription());
    }
}
