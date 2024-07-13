package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import AcceptanceTests.RealToTest;
import DomainLayer.Market.Market;
import PresentationLayer.Application;
import Util.SupplyServiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;


import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
@ContextConfiguration(classes = {Application.class, RealToTest.class})
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AddingExternalSupplyServices {

    @Autowired
    private Market market;


    @BeforeEach
    public void setUp() {
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
