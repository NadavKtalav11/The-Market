package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        int systemManagerId = 77;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        String supplyServiceName = "Hovalot";
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        // Act and Assert
        assertDoesNotThrow(() -> {
            market.addExternalSupplyService(licensedDealerNumber, supplyServiceName, countries,cities, systemManagerId);
        });
    }

    @Test
    public void testAddExternalSupplyServiceFailureNotSystemManager() {
        // Arrange
        int systemManagerId = 77;
        int nonManagerId = 2;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = 12345;
        market.getSystemManagerIds().add(systemManagerId);
        String supplyServiceName = "Hovalot";
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(licensedDealerNumber, supplyServiceName, countries,cities, nonManagerId);
        });

        // Optionally check the exception message
        assertEquals("Only system manager is allowed to add new external supply service", exception.getMessage());
    }

    @Test
    public void testAddExternalSupplytServiceFailureInvalidDetails() {
        // Arrange
        int systemManagerId = 77;
        market.getSystemManagerIds().add(systemManagerId);
        int licensedDealerNumber = -1;
        market.getSystemManagerIds().add(systemManagerId);
        String supplyServiceName = "Hovalot";
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.addExternalSupplyService(licensedDealerNumber, supplyServiceName, countries,cities, systemManagerId);
        });

        // Optionally check the exception message
        assertEquals("The system has not been able to add the supply service due to invalid details", exception.getMessage());
    }


}
