package DomainLayer;


import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.PaymentServices.ExternalPaymentService;
import DomainLayer.Store.StoreFacade;
import DomainLayer.SupplyServices.ExternalSupplyService;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import DomainLayer.User.UserFacade;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.CartDTO;

import Util.UserDTO;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;

import java.util.*;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class externalConnections {
    private static BridgeToTests impl;
    private static Market market = new Market();
    private UserFacade userFacade;
    private StoreFacade storeFacade;


    @BeforeAll()
    public static void setUp() throws Exception {
        market = new Market();
        market.init();
    }


    @Test
    @Order(1)
    public void checkHandShake() throws Exception {
        assertTrue(market.checkHandShake());

    }

    @Test
    @Order(2)
    public void checkPaymentSuccess() throws Exception {
        Map<String, Map<String, List<Integer>>> products = new HashMap<>();
        Map<String, List<Integer>> products1 = new HashMap<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        products1.put("bam", list1);
        products.put("store1", products1);
        ExternalPaymentService externalPaymentService = market.getPaymentServicesFacade().getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        CartDTO cartDTO = new CartDTO("130", 5500, products);
        PaymentDTO paymentDTO = new PaymentDTO("20444444", "David David", "USD", "2222333344445555", 982, 6, 2030);
        int res = externalPaymentService.payWithCard(1000, paymentDTO, "11", cartDTO.getStoreToProducts(), "4545");
        assertTrue(res>=10000);
        assertTrue(res<=100000);

    }


    @Test
    @Order(3)
    public void checkPaymentAndCancelSuccess() throws Exception {

        Map<String, Map<String, List<Integer>>> products = new HashMap<>();
        Map<String, List<Integer>> products1 = new HashMap<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        products1.put("bam", list1);
        products.put("store1", products1);
        ExternalPaymentService externalPaymentService = market.getPaymentServicesFacade().getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        CartDTO cartDTO = new CartDTO("130", 5500, products);
        PaymentDTO paymentDTO = new PaymentDTO("20444444", "David David", "USD", "2222333344445555", 982, 6, 2030);
        int res = externalPaymentService.payWithCard(1000, paymentDTO, "11", cartDTO.getStoreToProducts(), "4545");
        int res1= externalPaymentService.cancelPayment(res);
        assertEquals(1,res1);


    }

    @Test
    @Order(4)
    public void checkSupplySuccess() throws Exception {

        ExternalSupplyService supplyServices = market.getSupplyServicesFacade().getAllSupplyServices().get("https://damp-lynna-wsep-1984852e.koyeb.app/");
        int res = supplyServices.createSupply("david", "Israel", "Ashdod", "Elul");
        assertTrue(res>=10000);
        assertTrue(res<=100000);

    }

    @Test
    @Order(5)
    public void checkSupplyAndCancelSuccess() throws Exception {
        ExternalSupplyService supplyServices = market.getSupplyServicesFacade().getAllSupplyServices().get("https://damp-lynna-wsep-1984852e.koyeb.app/");
        int res = supplyServices.createSupply("david", "Israel", "Ashdod", "Elul");
        int res1= supplyServices.cancelSupply(res);
        assertEquals(1,res1);
    }
}