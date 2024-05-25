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
    }

    @Test
    public void testAddExternalService() {
        // Create mocks for parameters
        int licensedDealerNumber = 12345;
        String supplyServiceName = "MockService";
        HashSet<String> countries = Mockito.mock(HashSet.class);
        HashSet<String> cities = Mockito.mock(HashSet.class);

        // Mock externalSupplyService
        Map<Integer, ExternalSupplyService> externalSupplyServiceMapMock = Mockito.mock(HashMap.class);
        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);
      //  doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        boolean result = supplyServicesFacadeSpy.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        // Verify that externalSupplyService is called correctly
        verify(externalSupplyServiceMapMock).put(licensedDealerNumber, new ExternalSupplyService(licensedDealerNumber, supplyServiceName, countries, cities));

        // Check result
        assertTrue(result);
    }

    @Test
    public void testCheckAvailableExternalSupplyService() {
        // Mock externalSupplyService
        ExternalSupplyService externalSupplyServiceMock = Mockito.mock(ExternalSupplyService.class);
        when(externalSupplyServiceMock.checkAreaAvailability("MockCountry", "MockCity")).thenReturn(true);

        // Mock externalSupplyServiceMap
        Map<Integer, ExternalSupplyService> externalSupplyServiceMapMock = new HashMap<>();
        externalSupplyServiceMapMock.put(1, externalSupplyServiceMock);
        SupplyServicesFacade supplyServicesFacadeSpy = spy(supplyServicesFacade);
    //    doReturn(externalSupplyServiceMapMock).when(supplyServicesFacadeSpy).getExternalSupplyServiceMap();

        // Call method
        int result = supplyServicesFacadeSpy.checkAvailableExternalSupplyService("MockCountry", "MockCity");

        // Verify that externalSupplyService is called correctly
        verify(externalSupplyServiceMock).checkAreaAvailability("MockCountry", "MockCity");

        // Check result
        assertEquals(1, result);
    }

    // Add more test cases for other methods as needed

}
