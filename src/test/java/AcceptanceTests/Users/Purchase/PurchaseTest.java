package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import Util.PaymentDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.concurrent.TimeoutException;

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
         impl.init("KobiM", "27/4/95", "Israel","Beer Sheva","Mesada","kobi Menashe", "Password123",
                 1, "payementService", "kobi@gmail.com", 2, "supplyService", countries, cities);

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
        userDTO = new UserDTO("newUser1", "12/12/2000", "Israel", "BeerSheva", "bialik", "noa");
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

    //todo David implement this already
//    @Test
//    public void purchaseWithInvalidPaymentDetailsTest() {
//        // Invalid payment details
//        PaymentDTO invalidPaymentDTO = new PaymentDTO("holderName", "1111222233334444", 13, 2025, "123", "holderID");
//
//        Exception exception = assertThrows(Exception.class, () -> {
//            impl.purchase(userID1, invalidPaymentDTO, userDTO);
//        });
//
//        assertEquals("There is a problem with the provided payment measure or details of the order.\n", exception.getMessage());
//    }

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
         impl.addProductToBasket("Shoes", 13, storeID, userID1);

         Exception exception = assertThrows(Exception.class, () -> {
             Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                     paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
             assertFalse(response.isSuccess());
         });
         assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), exception.getMessage());
     }

     @Test
     public void productNotExistTest() {
         impl.addProductToBasket("pants", 5, storeID, userID1);

         Exception exception = assertThrows(Exception.class, () -> {
             Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                     paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
             assertFalse(response.isSuccess());
         });

         assertEquals(ExceptionsEnum.productNotExistInStore.toString(), exception.getMessage());
     }

     @Test
     public void productQuantityIsNegative() {
         impl.addProductToBasket("shoes", -1,storeID, userID1);

         Exception exception = assertThrows(Exception.class, () -> {
             Response<String> response =impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                     paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
             assertFalse(response.isSuccess());
         });

         assertEquals(ExceptionsEnum.productQuantityIsNegative.toString(), exception.getMessage());
     }

//     @Test
//     public void emptyCartTest() {
//         impl.removeProductFromBasket("Milk", "0", "1");
//         impl.removeProductFromBasket("Cheese", "0", "1");
//         impl.removeProductFromBasket("Yogurt", "0", "1");

//         Exception exception = assertThrows(Exception.class, () -> {
//             Response<Integer> response =impl.checkingCartValidationBeforePurchase("0", "Israel", "Beer Sheva", "Mesada");
//             assertFalse(response.isSuccess());
//         });

//         assertEquals(ExceptionsEnum.userCartIsEmpty.toString(), exception.getMessage());
//     }

//     @Test
//     public void purchasePolicyInvalidTest() {
//         // TODO: 31/05/2024 change this test to use mock
//         // TODO: 31/05/2024 maybe need to add also test for discount policy

//         Exception exception = assertThrows(Exception.class, () -> {
//             Response<Integer> response =impl.checkingCartValidationBeforePurchase("0", "Israel", "Beer Sheva", "Mesada");
//             assertFalse(response.isSuccess());
//         });

//         assertEquals(ExceptionsEnum.purchasePolicyIsNotMet.toString(), exception.getMessage());
//     }

//     @Test
//     public void shippingInvalidTest() {
//         Exception exception = assertThrows(Exception.class, () -> {
//             Response<Integer> response =impl.checkingCartValidationBeforePurchase("0", "Israel", "Tel Aviv", "Mesada");
//             assertFalse(response.isSuccess());
//         });

//         assertEquals(ExceptionsEnum.ExternalSupplyServiceIsNotAvailable.toString(), exception.getMessage());
//     }
}
