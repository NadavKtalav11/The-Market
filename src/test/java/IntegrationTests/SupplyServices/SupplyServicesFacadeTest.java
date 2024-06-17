package IntegrationTests.SupplyServices;

import DomainLayer.SupplyServices.ExternalSupplyService;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import Util.SupplyServiceDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SupplyServicesFacadeTest {

    private SupplyServicesFacade supplyServicesFacade;

    private final String licensedDealerNumber = "123";
    private final String supplyServiceName = "Test Supply Service";
    private final HashSet<String> countries = new HashSet<>();
    private final HashSet<String> cities = new HashSet<>();

    @BeforeEach
    public void setUp() {
        // Reset the SupplyServicesFacade singleton for each test
        supplyServicesFacade = SupplyServicesFacade.getInstance().newForTest();

        // Add test data
        countries.add("TestCountry");
        cities.add("TestCity");
    }

    @Test
    public void testGetInstance() {
        SupplyServicesFacade instance = SupplyServicesFacade.getInstance();
        assertNotNull(instance);
        assertSame(supplyServicesFacade, instance);
    }

    @Test
    public void testAddExternalServiceWithParams() {
        boolean added = supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);
        assertTrue(added);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceById(licensedDealerNumber);
        assertNotNull(service);
        assertEquals(licensedDealerNumber, service.getLicensedDealerNumber());
    }

    @Test
    public void testAddExternalServiceWithDTO() {
        SupplyServiceDTO supplyServiceDTO = new SupplyServiceDTO(licensedDealerNumber, supplyServiceName, countries, cities);

        boolean added = supplyServicesFacade.addExternalService(supplyServiceDTO);
        assertTrue(added);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceById(licensedDealerNumber);
        assertNotNull(service);
        assertEquals(licensedDealerNumber, service.getLicensedDealerNumber());
    }

    @Test
    public void testRemoveExternalService() {
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);
        supplyServicesFacade.removeExternalService(licensedDealerNumber);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceById(licensedDealerNumber);
        assertNull(service);
    }

    @Test
    public void testCheckAvailableExternalSupplyService() {
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        String result = supplyServicesFacade.checkAvailableExternalSupplyService("TestCountry", "TestCity");
        assertEquals(licensedDealerNumber, result);

        String resultNotFound = supplyServicesFacade.checkAvailableExternalSupplyService("NonExistentCountry", "NonExistentCity");
        assertEquals("-2", resultNotFound);
    }

    @Test
    public void testGetAllSupplyServices() {
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        Map<String, ExternalSupplyService> allServices = supplyServicesFacade.getAllSupplyServices();
        assertEquals(1, allServices.size());
        assertTrue(allServices.containsKey(licensedDealerNumber));
    }

    @Test
    public void testCreateShiftingDetails() {
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);

        boolean result = supplyServicesFacade.createShiftingDetails(licensedDealerNumber, "User", "TestCountry", "TestCity", "TestAddress");
        assertTrue(result);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceById(licensedDealerNumber);
        assertNotNull(service);
        assertTrue(service.createShiftingDetails("User", "TestCountry", "TestCity", "TestAddress"));
    }

    @Test
    public void testReset() {
        supplyServicesFacade.addExternalService(licensedDealerNumber, supplyServiceName, countries, cities);
        supplyServicesFacade.reset();

        assertTrue(supplyServicesFacade.getAllSupplyServices().isEmpty());
    }
}