package DomainLayer.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import static org.junit.jupiter.api.Assertions.*;

import DomainLayer.Store.StoreFacade;
import DomainLayer.User.UserFacade;
import Util.ExceptionsEnum;
import Util.PaymentServiceDTO;
import Util.SupplyServiceDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

import Util.PaymentDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class StateInit {
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
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.userFacade = UserFacade.getInstance();
        this.storeFacade = StoreFacade.getInstance();

        this.market = new Market(userFacade, storeFacade);


    }

    @Test
    public void successfulInitTest() throws Exception {
        assertFalse(market.isInitialized());
        assertEquals(0, market.getSystemManagerIds().size());
        assertEquals(0, userFacade.getMembers().getAll().size());
        assertEquals(0, storeFacade.getStores().size());



        // Act
        market.init(new PaymentServiceDTO(licensedDealerNumber,
                paymentServiceName, url), new SupplyServiceDTO(licensedDealerNumber1, supplyServiceName, countries, cities));

        // Assuming there's a method isInitialized() that returns whether the system is initialized
        assertTrue(market.isInitialized());
        assertEquals(1, market.getSystemManagerIds().size());
        String memberID = market.getSystemManagerIds().iterator().next();

        // assert the system manager details match the details in the config file
        assertEquals("u1", userFacade.getMembers().get(memberID).getUsername());
        assertEquals("19/09/1996", userFacade.getMembers().get(memberID).getBirthday());
        assertEquals("Israel", userFacade.getMembers().get(memberID).getCountry());
        assertEquals("Ashdod", userFacade.getMembers().get(memberID).getCity());
        assertEquals("Elul", userFacade.getMembers().get(memberID).getAddress());
        assertEquals("David Volodarsky", userFacade.getMembers().get(memberID).getName());

        assertEquals(6, userFacade.getMembers().getAll().size());
        assertEquals(1, storeFacade.getStores().size());
        String storeID = storeFacade.getStores().iterator().next();
        assertTrue( storeFacade.getStoreByID(storeID).getStoreProducts().containsKey("Bamba"));

    }
}