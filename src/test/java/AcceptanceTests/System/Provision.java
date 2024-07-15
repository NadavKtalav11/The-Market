package AcceptanceTests.System;

//import AcceptanceTests.BridgeToTests;
//import AcceptanceTests.ProxyToTest;
//import AcceptanceTests.RealToTest;
//import DomainLayer.AuthenticationAndSecurity.AuthenticationAndSecurityFacade;
//import DomainLayer.Market.Market;
//import DomainLayer.PaymentServices.PaymentServicesFacade;
//import DomainLayer.Store.StoreFacade;
//import DomainLayer.SupplyServices.ExternalSupplyService;
//import DomainLayer.SupplyServices.SupplyServicesFacade;
//import DomainLayer.User.User;
//import DomainLayer.User.UserFacade;
//import PresentationLayer.Application;
//import Util.*;
//import jakarta.inject.Inject;
//import org.junit.jupiter.api.*;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.ContextConfiguration;
//
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.*;
//
//@ContextConfiguration(classes = {Application.class, RealToTest.class})
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
////@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//public class Provision {
//
//    @Inject
//    private Market market;
//
//    @Mock
//    private PaymentServicesFacade paymentServicesFacade;
//
//    @Mock
//    private SupplyServicesFacade supplyServicesFacade;
//
//    @Mock
//    private UserFacade userFacade;
//
//    @Mock
//    private StoreFacade storeFacade;
//
//
//
//    @Mock
//    private AuthenticationAndSecurityFacade authenticationAndSecurityFacade;
//
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        //this.market = Market.getInstance();
//        this.paymentServicesFacade = market.getPaymentServiceFacade();
//        this.supplyServicesFacade = market.getSupplyServicesFacade();
//        this.authenticationAndSecurityFacade = market.getAuthenticationAndSecurityFacade();
//        paymentServicesFacade.clearPaymentServices();
//
//        this.userFacade = market.getUserFacade();
//        //market = new Market(paymentServicesFacade, supplyServicesFacade,authenticationAndSecurityFacade);
//      //  resetSingletons();
//
//    }
//
//
//    @Test
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
//    public void notExitingSupplyServiceTest() throws Exception {
//        PaymentDTO paymentDTO = new PaymentDTO("13022303", "david", "USD","9868986898689868", 100, 6,2030);
//        UserDTO userDTO = new UserDTO("testUser", "birth", "israel", "bash", "bash", "David", "testUser");
//        // Mock the necessary methods
//        UserFacade userFacade = Mockito.mock(UserFacade.class);
//        StoreFacade storeFacade = Mockito.mock(StoreFacade.class);
//        AuthenticationAndSecurityFacade authenticationAndSecurityFacade =  Mockito.mock(AuthenticationAndSecurityFacade.class);
//        Market market1 = new Market(userFacade, authenticationAndSecurityFacade,storeFacade);
//        String userId = "testUser";
//        String memberId = "member123";
//        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId);
//        Mockito.when(userFacade.isMember(userId)).thenReturn(true);
//        Mockito.when(userFacade.getMemberIdByUserId(userId)).thenReturn(memberId);
//        Mockito.when(authenticationAndSecurityFacade.getToken(memberId)).thenReturn("validToken");
//        Mockito.when(authenticationAndSecurityFacade.validateToken("validToken")).thenReturn(true);
//        Mockito.doNothing().when(userFacade).isUserCartEmpty(userId); // Ensure no exception is thrown
//
//        // Mock other necessary methods for the test
//        Mockito.when(userFacade.getCartStoresByUser(userId)).thenReturn(List.of("store1"));
//        Mockito.when(userFacade.getCartProductsByStoreAndUser("store1", userId)).thenReturn(Map.of("product1", List.of(1)));
//        Mockito.when(storeFacade.getProductsDTOSByProductsNames(anyMap(), eq("store1"))).thenReturn(List.of(new ProductDTO()));
//        Mockito.doNothing().when(storeFacade).checkQuantityAndPrice(anyString(), anyInt(), anyString());
////        Mockito.doNothing().when(storeFacade).checkPurchasePolicy(userDTO, anyList(), "store1");
//        Mockito.when(userFacade.getCartPriceByUser(userId)).thenReturn(100);
////        Mockito.when(storeFacade.calcDiscountPolicy(userDTO, anyList(), "store1")).thenReturn(90);
//
//
//        assertEquals(0, supplyServicesFacade.getAllSupplyServices().size());
//
//        Exception exception = assertThrows(Exception.class, () -> {market1.purchaseForTest(paymentDTO, userDTO);
//        });
//
//        assertEquals( ExceptionsEnum.NoExternalSupplyService.toString(), exception.getMessage());
//
//    }
//}
