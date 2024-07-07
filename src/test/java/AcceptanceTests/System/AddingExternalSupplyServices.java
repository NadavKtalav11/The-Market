package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import Util.SupplyServiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Util.ExceptionsEnum;


import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AddingExternalSupplyServices {
    private static BridgeToTests impl;
    private Market market;


    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.market = Market.getInstance();

    }

    @Test
    public void testAddExternalSupplyServiceSuccess() {
        // Arrange
        String url = ".com";
        String systemManagerId = "user77";
        market.getSystemManagerIds().add(systemManagerId);
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalSupplyService(url, systemManagerId);
        });
    }

    @Test
    public void testAddExternalSupplyServiceFailureNotSystemManager() {
        // Arrange
        String url = ".com";
        String systemManagerId = "user77";
        String nonManagerId = "user2";
        market.getSystemManagerIds().add(systemManagerId);
        market.getSystemManagerIds().add(systemManagerId);
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(url, nonManagerId);
        });

        //  check the exception message
        assertEquals(ExceptionsEnum.SystemManagerSupplyAuthorization.toString(), exception.getMessage());
    }

    @Test
    public void testAddExternalSupplyServiceFailureInvalidDetails() {
        // Arrange
        String url = null;

        String systemManagerId = "user77";
        market.getSystemManagerIds().add(systemManagerId);
        market.getSystemManagerIds().add(systemManagerId);
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(url, systemManagerId);
        });

        //  check the exception message
        assertEquals(ExceptionsEnum.InvalidSupplyServiceParameters.toString(), exception.getMessage());
    }


}
