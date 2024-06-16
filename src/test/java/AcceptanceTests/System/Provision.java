package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;
import DomainLayer.Market.Market;
import DomainLayer.PaymentServices.PaymentServicesFacade;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.ExternalSupplyService;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.User;
import DomainLayer.User.UserFacade;
import Util.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class Provision {
    private static BridgeToTests impl;
    private Market market;

    @Mock
    private PaymentServicesFacade paymentServicesFacade;

    @Mock
    private SupplyServicesFacade supplyServicesFacade;

    @Mock
    private UserFacade userFacade;

    @Mock
    private StoreFacade storeFacade;



    @Mock
    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        MockitoAnnotations.openMocks(this);


        this.market = Market.getInstance();
        this.paymentServicesFacade = PaymentServicesFacade.getInstance();
        this.supplyServicesFacade = SupplyServicesFacade.getInstance();
        this.authenticationAndSecurityFacade = AuthenticationAndSecurityFacade.getInstance();

        this.userFacade = userFacade.getInstance();

        User mockUser = mock(User.class);

        resetSingletons();

    }
    private  void resetSingletons() {
        // Clear the state in Market singleton
        market.getSystemManagerIds().clear();

        // Clear the state in PaymentServicesFacade singleton
        paymentServicesFacade.clearPaymentServices();

        //supplyServicesFacade.clearPaymentServices();

        // Add any other necessary resets for singletons here
    }

    @Test
    public void successfulTransferTest() throws Exception {
// Mock the necessary methods
        UserFacade userFacade = Mockito.mock(UserFacade.class);

        //SupplyServicesFacade supplyServicesFacade1 = Mockito.mock(SupplyServicesFacade.class);
        SupplyServicesFacade supplyServicesFacade1 = SupplyServicesFacade.getInstance();
        StoreFacade storeFacade = Mockito.mock(StoreFacade.class);
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade =  Mockito.mock(AuthenticationAndSecurityFacade.class);
        Market market1 = new Market(userFacade, authenticationAndSecurityFacade,storeFacade,supplyServicesFacade1);
        String userId = "testUser";
        String memberId = "member123";
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId);
        Mockito.when(userFacade.isMember(userId)).thenReturn(true);
//        //         String userName = this.userFacade.getUserByID(user_ID).getName();
//        Mockito.when(userFacade.getUserByID(anyString()).the(Mockito.mock(User.class)));
////         Mockito.when(User.getName().thenReturn(Mockito.mock(User.class));
//        Mockito.doNothing().when(market1).createShiftingDetails(anyString(),anyString(),
//                anyString(),anyString(),anyString());

        Mockito.when(userFacade.getMemberIdByUserId(userId)).thenReturn(memberId);
        Mockito.when(authenticationAndSecurityFacade.getToken(memberId)).thenReturn("validToken");
        Mockito.when(authenticationAndSecurityFacade.validateToken("validToken")).thenReturn(true);
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId); // Ensure no exception is thrown

        // Mock other necessary methods for the test
        Mockito.when(userFacade.getCartStoresByUser(userId)).thenReturn(List.of("store1"));
        Mockito.when(userFacade.getCartProductsByStoreAndUser("store1", userId)).thenReturn(Map.of("product1", List.of(1)));
        Mockito.when(storeFacade.getProductsDTOSByProductsNames(anyMap(), eq("store1"))).thenReturn(List.of(new ProductDTO()));
        Mockito.doNothing().when(storeFacade).checkQuantity(anyString(), anyInt(), anyString());
//        Mockito.when(storeFacade.checkPolicies(any(UserDTO.class), anyList(), eq("store1"))).thenReturn(true);
        Mockito.when(userFacade.getCartPriceByUser(userId)).thenReturn(100);
//        Mockito.when(storeFacade.calculateTotalCartPriceAfterDiscount(eq("store1"), anyMap(), eq(100))).thenReturn(90);
        assertEquals(0, supplyServicesFacade1.getAllSupplyServices().size());
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Ashdod");

        supplyServicesFacade1.addExternalService(new SupplyServiceDTO("123","supply", countries,cities));
        assertEquals(1, supplyServicesFacade1.getAllSupplyServices().size());

        PaymentDTO paymentDTO = new PaymentDTO("holder", "12334", 334, 11, 2027);
        UserDTO userDTO = new UserDTO("testUser", "birth", "israel", "Israel", "Ashdod", "David", "testUser");
        assertDoesNotThrow(() -> {market1.purchaseForTest(paymentDTO, userDTO);
        });

    }
    //
    @Test
    public void noExternalSupplyServiceForCityTest() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("holder", "12334", 334, 11, 2027);
        UserDTO userDTO = new UserDTO("testUser", "birth", "srael", "Israel", "bash", "David", "testUser");


        // Mock the necessary methods
        UserFacade userFacade = Mockito.mock(UserFacade.class);
        //SupplyServicesFacade supplyServicesFacade1 = Mockito.mock(SupplyServicesFacade.class);
        SupplyServicesFacade supplyServicesFacade1 = SupplyServicesFacade.getInstance();
        StoreFacade storeFacade = Mockito.mock(StoreFacade.class);
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade =  Mockito.mock(AuthenticationAndSecurityFacade.class);
        Market market1 = new Market(userFacade, authenticationAndSecurityFacade,storeFacade,supplyServicesFacade1);
        String userId = "testUser";
        String memberId = "member123";
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId);
        Mockito.when(userFacade.isMember(userId)).thenReturn(true);
        Mockito.when(userFacade.getMemberIdByUserId(userId)).thenReturn(memberId);
        Mockito.when(authenticationAndSecurityFacade.getToken(memberId)).thenReturn("validToken");
        Mockito.when(authenticationAndSecurityFacade.validateToken("validToken")).thenReturn(true);
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId); // Ensure no exception is thrown

        // Mock other necessary methods for the test
        Mockito.when(userFacade.getCartStoresByUser(userId)).thenReturn(List.of("store1"));
        Mockito.when(userFacade.getCartProductsByStoreAndUser("store1", userId)).thenReturn(Map.of("product1", List.of(1)));
        Mockito.when(storeFacade.getProductsDTOSByProductsNames(anyMap(), eq("store1"))).thenReturn(List.of(new ProductDTO()));
        Mockito.doNothing().when(storeFacade).checkQuantity(anyString(), anyInt(), anyString());
//        Mockito.doNothing().when(storeFacade).checkPurchasePolicy(userDTO, anyList(), "store1");
        Mockito.when(userFacade.getCartPriceByUser(userId)).thenReturn(100);
//        Mockito.when(storeFacade.calcDiscountPolicy(userDTO, anyList(), "store1")).thenReturn(90);
        assertEquals(0, supplyServicesFacade1.getAllSupplyServices().size());
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Ashdod");

        supplyServicesFacade1.addExternalService(new SupplyServiceDTO("123","supply", countries,cities));
        assertEquals(1, supplyServicesFacade1.getAllSupplyServices().size());


        Exception exception = assertThrows(Exception.class, () -> {market1.purchaseForTest(paymentDTO, userDTO);
        });

        assertEquals( ExceptionsEnum.ExternalSupplyServiceIsNotAvailableForArea.toString(), exception.getMessage());
    }

    @Test
    public void noExternalSupplyServiceForCountryTest() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("holder", "12334", 334, 11, 2027);
        UserDTO userDTO = new UserDTO("testUser", "birth", "srael", "Israel", "bash", "David", "testUser");

        // Mock the necessary methods
        UserFacade userFacade = Mockito.mock(UserFacade.class);
        //SupplyServicesFacade supplyServicesFacade1 = Mockito.mock(SupplyServicesFacade.class);
        SupplyServicesFacade supplyServicesFacade1 = SupplyServicesFacade.getInstance();
        StoreFacade storeFacade = Mockito.mock(StoreFacade.class);
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade =  Mockito.mock(AuthenticationAndSecurityFacade.class);
        Market market1 = new Market(userFacade, authenticationAndSecurityFacade,storeFacade,supplyServicesFacade1);
        String userId = "testUser";
        String memberId = "member123";
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId);
        Mockito.when(userFacade.isMember(userId)).thenReturn(true);
        Mockito.when(userFacade.getMemberIdByUserId(userId)).thenReturn(memberId);
        Mockito.when(authenticationAndSecurityFacade.getToken(memberId)).thenReturn("validToken");
        Mockito.when(authenticationAndSecurityFacade.validateToken("validToken")).thenReturn(true);
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId); // Ensure no exception is thrown

        // Mock other necessary methods for the test
        Mockito.when(userFacade.getCartStoresByUser(userId)).thenReturn(List.of("store1"));
        Mockito.when(userFacade.getCartProductsByStoreAndUser("store1", userId)).thenReturn(Map.of("product1", List.of(1)));
        Mockito.when(storeFacade.getProductsDTOSByProductsNames(anyMap(), eq("store1"))).thenReturn(List.of(new ProductDTO()));
        Mockito.doNothing().when(storeFacade).checkQuantity(anyString(), anyInt(), anyString());
//        Mockito.doNothing().when(storeFacade).checkPurchasePolicy(userDTO, anyList() , eq("store1"));
        Mockito.when(userFacade.getCartPriceByUser(userId)).thenReturn(100);
//        Mockito.when(storeFacade.calcDiscountPolicy(userDTO, anyList(), "store1")).thenReturn(90);
        assertEquals(0, supplyServicesFacade1.getAllSupplyServices().size());
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("France");
        cities.add("Ashdod");

        supplyServicesFacade1.addExternalService(new SupplyServiceDTO("123","supply", countries,cities));
        assertEquals(1, supplyServicesFacade1.getAllSupplyServices().size());


        Exception exception = assertThrows(Exception.class, () -> {market1.purchaseForTest(paymentDTO, userDTO);
        });

        assertEquals( ExceptionsEnum.ExternalSupplyServiceIsNotAvailableForArea.toString(), exception.getMessage());
    }



    @Test
    public void notExitingSupplyServiceTest() throws Exception {
        PaymentDTO paymentDTO = new PaymentDTO("holder", "12334", 334, 11, 2027);
        UserDTO userDTO = new UserDTO("testUser", "birth", "israel", "bash", "bash", "David", "testUser");
        // Mock the necessary methods
        UserFacade userFacade = Mockito.mock(UserFacade.class);
        StoreFacade storeFacade = Mockito.mock(StoreFacade.class);
        AuthenticationAndSecurityFacade authenticationAndSecurityFacade =  Mockito.mock(AuthenticationAndSecurityFacade.class);
        Market market1 = new Market(userFacade, authenticationAndSecurityFacade,storeFacade);
        String userId = "testUser";
        String memberId = "member123";
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId);
        Mockito.when(userFacade.isMember(userId)).thenReturn(true);
        Mockito.when(userFacade.getMemberIdByUserId(userId)).thenReturn(memberId);
        Mockito.when(authenticationAndSecurityFacade.getToken(memberId)).thenReturn("validToken");
        Mockito.when(authenticationAndSecurityFacade.validateToken("validToken")).thenReturn(true);
        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId); // Ensure no exception is thrown

        // Mock other necessary methods for the test
        Mockito.when(userFacade.getCartStoresByUser(userId)).thenReturn(List.of("store1"));
        Mockito.when(userFacade.getCartProductsByStoreAndUser("store1", userId)).thenReturn(Map.of("product1", List.of(1)));
        Mockito.when(storeFacade.getProductsDTOSByProductsNames(anyMap(), eq("store1"))).thenReturn(List.of(new ProductDTO()));
        Mockito.doNothing().when(storeFacade).checkQuantity(anyString(), anyInt(), anyString());
//        Mockito.doNothing().when(storeFacade).checkPurchasePolicy(userDTO, anyList(), "store1");
        Mockito.when(userFacade.getCartPriceByUser(userId)).thenReturn(100);
//        Mockito.when(storeFacade.calcDiscountPolicy(userDTO, anyList(), "store1")).thenReturn(90);


        assertEquals(0, supplyServicesFacade.getAllSupplyServices().size());
        String systemManagerId = "77";
        market.getSystemManagerIds().add(systemManagerId);

        Exception exception = assertThrows(Exception.class, () -> {market1.purchaseForTest(paymentDTO, userDTO);
        });

        assertEquals( ExceptionsEnum.NoExternalSupplyService.toString(), exception.getMessage());

    }

//    @Test
//    public void invalidDetailsTest() {
//
//    }

}
