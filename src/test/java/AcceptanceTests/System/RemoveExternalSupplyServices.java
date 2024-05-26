package AcceptanceTests.System;
import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import DomainLayer.Market.Market;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class RemoveExternalSupplyServices {
    private static BridgeToTests impl;
    private Market market;



    @BeforeEach
    public void setUp() {
        impl = new ProxyToTest("Real");
        this.market = Market.getInstance();
        market.getSystemManagerIds().add(77);

    }

    @Test
    public void testRemoveExternalPaymentServiceSuccess() throws Exception {
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        market.addExternalSupplyService(12345, "Hovalot", countries,cities, 77);
        market.addExternalSupplyService(67890, "Hovalot1", countries,cities,77);

        // Act and Assert
        assertDoesNotThrow(() -> {
            market.removeExternalSupplyService(12345, 77);
        });
    }

    @Test
    public void testRemoveExternalSupplyServiceFailureNotSystemManager() throws Exception {
        // Arrange
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        market.addExternalSupplyService(12345, "Hovalat", countries,cities,77);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalSupplyService(12345, 2); // 2 is not a system manager ID
        });

        // Optionally check the exception message
        assertEquals("Only system manager is allowed to remove external supply service", exception.getMessage());
    }

    @Test
    public void testRemoveExternalPaymentServiceFailureOnlyOneService() throws Exception {
        // Arrange
        HashSet<String> countries = new HashSet<>();
        HashSet<String> cities = new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");
        market.addExternalSupplyService(12345, "Hovalot", countries,cities,77);

        // Act and Assert
        Exception exception = assertThrows(Exception.class, () -> {
            market.removeExternalSupplyService(12345, 77);
        });

        // Optionally check the exception message
        assertEquals("There must remain at least one external supply service in the system", exception.getMessage());
    }

}
