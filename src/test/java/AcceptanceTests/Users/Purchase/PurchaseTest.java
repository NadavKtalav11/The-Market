package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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
        impl.setUserConfirmationPurchase(userID2);
        Response<String> result = impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(), paymentDTO.getCvv(), paymentDTO.getMonth(), paymentDTO.getYear(), paymentDTO.getHolderId());

        assertTrue(result.isSuccess());
    }

    @Test
    public void purchaseWithTimeoutTest() {
        // This simulates the user not responding within the 5-minute limit
        Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
        assertEquals(ExceptionsEnum.TimeExpired.toString(), response.getDescription());
    }

    @Test
    public void purchaseWithEmptyCartTest() {
        impl.removeProductFromBasket("Milk", storeID, userID2);
        impl.removeProductFromBasket("Cheese", storeID, userID2);
        impl.removeProductFromBasket("Yogurt" , storeID, userID2);
        impl.setUserConfirmationPurchase(userID2);

        Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
        assertEquals(ExceptionsEnum.userCartIsEmpty.toString(), response.getDescription());
    }

    @Test
     public void productQuantityUnavailableTest() {
         impl.updateProductInStore(userID1, storeID, "Cheese", 20, 1, "Cheddar", "Dairy");
        impl.setUserConfirmationPurchase(userID2);

        Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                 paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());
         assertEquals(ExceptionsEnum.productQuantityNotExist.toString(), response.getDescription());
     }

     @Test
     public void productNotExistTest() {
         impl.removeProductFromStore(userID1, storeID, "Milk");
         impl.setUserConfirmationPurchase(userID2);


         Response<String> response = impl.purchase(userID2,userDTO.getCountry(), userDTO.getCity(),userDTO.getAddress(),
                 paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.productNotExistInStore.toString(), response.getDescription());
     }

     @Test
     public void purchasePolicyInvalidTest() {
         impl.addPurchaseRuleToStore(new ArrayList<>(Arrays.asList(5)), new ArrayList<>(), userID1, storeID);
         impl.setUserConfirmationPurchase(userID2);


         Response<String> response =impl.purchase(userID2, userDTO.getCountry(), userDTO.getCity(), userDTO.getAddress(), paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.purchasePolicyIsNotMet.toString(), response.getDescription());
     }

     @Test
     public void shippingInvalidTest() {
         impl.setUserConfirmationPurchase(userID2);
         Response<String> response =impl.purchase(userID2, "Israel", "Tel Aviv", "Rothschild", paymentDTO.getCreditCardNumber(),paymentDTO.getCvv(),paymentDTO.getMonth(), paymentDTO.getYear(),paymentDTO.getHolderId());
         assertFalse(response.isSuccess());

         assertEquals(ExceptionsEnum.ExternalSupplyServiceIsNotAvailable.toString(), response.getDescription());
     }
}
