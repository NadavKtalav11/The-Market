package DomainLayer.SupplyServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplyServicesFacadeTest {
    private SupplyServicesFacade supplyServicesFacade;

    @BeforeEach
    public void setUp() {
        supplyServicesFacade = SupplyServicesFacade.getInstance();
        supplyServicesFacade.reset(); // Reset the state before each test

    }

    @Test
    public void testAddExternalService() {
        // Create mocks for parameters
        String licensedDealerNumber = "12345";
        String supplyServiceName = "MockService";
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");

        // Mock externalSupplyService
        Map<Integer, ExternalSupplyService> externalSupplyServiceMapMock = Mockito.mock(HashMap.class);
        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);
      //  doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        boolean result = supplyServicesFacadeSpy.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        // Verify that externalSupplyService is called correctly
//        verify(externalSupplyServiceMapMock).put(licensedDealerNumber, new ExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities));

        // Check result
        assertTrue(result);
    }

    @Test
    public void testCheckAvailableExternalSupplyService() {
        // Mock externalSupplyService


        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);

        String licensedDealerNumber = "12345";
        String supplyServiceName = "MockService";
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();
        countries.add("Israel");
        cities.add("Bash");

      supplyServicesFacadeSpy.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        //    doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        String result = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("Israel", "Bash");
        assertEquals("12345", result);

        String result1 = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("Israel", "Ashdod");
        assertEquals("-2", result1);
        ExternalSupplyService externalSupplyService = supplyServicesFacadeSpy.getExternalSupplyServiceById("12345");
        HashSet<String> cities1 =new HashSet<>();
        cities1.add("Ashdod");
        cities1.add("Haifa");
        externalSupplyService.addCities(cities1);
        String result2 = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("Israel", "Ashdod");
        assertEquals("12345", result2);


    }


}
