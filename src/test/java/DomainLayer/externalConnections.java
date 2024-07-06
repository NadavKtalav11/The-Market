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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class externalConnections {
    private static BridgeToTests impl;
    private Market market;
    private UserFacade userFacade;
    private StoreFacade storeFacade;

    String licensedDealerNumber = "12345";
    String paymentServiceName = "PayPal";
    String url = "http://example.com";
    String licensedDealerNumber1 = "67890";
    String supplyServiceName = "SupplyService";
    HashSet<String> countries = new HashSet<>(Arrays.asList("USA", "Canada"));
    HashSet<String> cities = new HashSet<>(Arrays.asList("New York", "Los Angeles"));


    @BeforeEach
    public void setUp() throws Exception {
        market = new Market();
        market.init();

    }

    @Test
    public void checkHandShake() throws Exception {
        assertTrue(market.checkHandShake());

    }

    @Test
    public void checkPaymentSuccess() throws Exception {
        Map<String, Map<String, List<Integer>>> products = new HashMap<>();
        Map<String, List<Integer>> products1 = new HashMap<>();
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        products1.put("bam", list1);
        products.put("store1", products1);
        ExternalPaymentService externalPaymentService = market.getPaymentServicesFacade().getPaymentServiceByURL("https://damp-lynna-wsep-1984852e.koyeb.app/");
        System.out.println(externalPaymentService.getUrl());
        CartDTO cartDTO = new CartDTO("130", 5500, products);
        UserDTO userDTO = new UserDTO("130", "daivd", "10.10", "Israel", "Ashdod", "elul", "david");
        PaymentDTO paymentDTO = new PaymentDTO("130", "david", "USD", "13013103030", 986, 6, 2030);
       // int res = externalPaymentService.payWithCard(cartDTO.getCartPrice(), paymentDTO, "11", cartDTO.getStoreToProducts(), "4545");
        //System.out.println(res);
        //   market.purchase(paymentDTO,userDTO,cartDTO);

//        market.payWithExternalPaymentService(cartDTO,paymentDTO,"130");
//    }
    }

    @Test
    public void checkSupplySuccess() throws Exception {
        ExternalSupplyService supplyServices = market.getSupplyServicesFacade().getAllSupplyServices().get("https://damp-lynna-wsep-1984852e.koyeb.app/");
        System.out.println(supplyServices.getSupplyURL());
        supplyServices.createSupply("david", "Isarel", "Ashdod", "Elul");
           // market.payWithExternalPaymentService(cartDTO,paymentDTO,"130");
//    }
    }
}