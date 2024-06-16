package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

public class PurchaseTest {

    private static BridgeToTests impl;
    private static String userID1;
    private static String userID2;
    private static String storeID;
    private static PaymentDTO paymentDTO;
    private static UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
         //Do what you need
         HashSet<String> countries = new HashSet<>();
         countries.add("Israel");
         HashSet<String> cities = new HashSet<>();
         cities.add("BeerSheva");
         impl.init(new UserDTO("0", "KobiM", "27/4/95", "Israel","Beer Sheva","Mesada","kobi Menashe"), "Password123",

                 new PaymentServiceDTO("1", "payementService", "kobi@gmail.com"),new SupplyServiceDTO("2", "supplyService", countries, cities));


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
        userDTO = new UserDTO(userID2, "newUser2", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa");
    }

    @Test
    public void successfulPurchaseTest() {
        assertTrue(impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId()).isSuccess());
    }

    //todo implement this test after implement 5 seconds
//    @Test
//    public void purchaseWithTimeoutTest() {
//        // This simulates the user not responding within the 5-minute limit
//        Exception exception = assertThrows(Exception.class, () -> {
//            impl.purchase(userID1,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
//                    paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
//        });
//    }

//    //todo David implement this already
    @Test
    public void purchaseWithInvalidPaymentDetailsTest() throws Exception {
        // Invalid payment details
        PaymentDTO invalidPaymentDTO = new PaymentDTO("holderName", "1111222233334444", 13, 12, 1990);
        Response<String> response = impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                    invalidPaymentDTO.getCreditCardNumber(), invalidPaymentDTO.getCvv(), invalidPaymentDTO.getMonth(), invalidPaymentDTO.getYear(), invalidPaymentDTO.getHolderId());


        assertEquals( ExceptionsEnum.InvalidCreditCardParameters.toString(), response.getDescription());
    }

    @Test
    public void purchaseWithEmptyCartTest() {
        impl.removeProductFromBasket("Milk", storeID, userID2);
        impl.removeProductFromBasket("Cheese", storeID, userID2);
        impl.removeProductFromBasket("Yogurt" , storeID, userID2);

        Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
        assertEquals(ExceptionsEnum.userCartIsEmpty.toString(), response.getDescription());
    }

    @Test
     public void productQuantityUnavailableTest() {
         impl.updateProductInStore(userID1, storeID, "Cheese", 20, 1, "Cheddar", "Dairy");
         Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                 paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());
         assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response.getDescription());
     }

     @Test
     public void productNotExistTest() {
         impl.removeProductFromStore(userID1, storeID, "Milk");

         Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                 paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
     }

     @Test
     public void purchasePolicyInvalidTest() {
         impl.addPurchaseRuleToStore(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(), userID1, storeID);

         Response<String> response =impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(), paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.purchasePolicyIsNotMet.toString(), response.getDescription());
     }

     @Test
     public void shippingInvalidTest() {
         Response<String> response =impl.purchase(userID2, "Israel", "Tel Aviv", "Rothschild", paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.ExternalSupplyServiceIsNotAvailableForArea.toString(), response.getDescription());
     }


    @Test
    public void concurrentPurchaseTest() throws InterruptedException, ExecutionException {

        final int NUM_ITERATIONS = 100;
        final String productName = "water";
        boolean allPassed = true;

        ExecutorService executor = Executors.newFixedThreadPool(2);
        ArrayList<Callable<Boolean>> tasks = new ArrayList<>();

        for (int i = 0; i < NUM_ITERATIONS; i++) {
            setUp();
            impl.addProductToStore(userID1, storeID, productName, 1, 1, "Milk 12%", "food");
            impl.addProductToBasket(productName, 1, storeID, userID2);
            impl.addProductToBasket(productName, 1, storeID, userID1);

            Callable<Boolean> user1Task = () -> {
                Response<String> response = impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(),
                        paymentDTO.getCreditCardNumber(), paymentDTO.getCvv(), paymentDTO.getMonth(), paymentDTO.getYear(), paymentDTO.getHolderId());
                return response.isSuccess();
            };

            Callable<Boolean> user2Task = () -> {
                Response<String> response = impl.purchase(userID1, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(),
                        paymentDTO.getCreditCardNumber(), paymentDTO.getCvv(), paymentDTO.getMonth(), paymentDTO.getYear(), paymentDTO.getHolderId());
                return response.isSuccess();
            };

            tasks.add(user1Task);
            tasks.add(user2Task);
            Future<Boolean> user1Future = executor.submit(tasks.get(0));
            Future<Boolean> user2Future = executor.submit(tasks.get(1));

            tasks.remove(0);
            tasks.remove(0);
            boolean user1Success = user1Future.get();
            boolean user2Success = user2Future.get();
            if (user1Success && user2Success || !user1Success && !user2Success) {
                System.out.println(i);
                allPassed = false;
                break;
            }
        }

        executor.shutdown();
        assertTrue(allPassed, "Concurrent purchase test failed.");
    }
}

