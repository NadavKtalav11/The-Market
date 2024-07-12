package IntegrationTests.SupplyServices;

import DomainLayer.Repositories.ExternalSupplyRepository;
import DomainLayer.SupplyServices.ExternalSupplyService;
import DomainLayer.SupplyServices.SupplyServicesFacade;
import PresentationLayer.Application;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ContextConfiguration(classes = {Application.class})
@SpringBootTest
public class SupplyServicesFacadeTest {

    @Autowired
    public ExternalSupplyRepository externalSupplyRepository;

    private SupplyServicesFacade supplyServicesFacade;

    private final String supplyURl = "https://damp-lynna-wsep-1984852e.koyeb.app/";

    @BeforeEach
    public void setUp() {
        // Reset the SupplyServicesFacade singleton for each test
        supplyServicesFacade = new SupplyServicesFacade(externalSupplyRepository);
        supplyServicesFacade.reset();
    }

    @AfterEach
    public void tearDown() {
        supplyServicesFacade.reset();
    }

    @Test
    public void testAddExternalServiceWithParams() {
        boolean added = supplyServicesFacade.addExternalService(supplyURl);
        assertTrue(added);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceByURL(supplyURl);
        assertNotNull(service);
        assertEquals(supplyURl, service.getSupplyURL());
    }

    @Test
    public void testRemoveExternalService() {
        supplyServicesFacade.addExternalService( supplyURl);
        supplyServicesFacade.removeExternalService(supplyURl);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceByURL(supplyURl);
        assertNull(service);
    }

    @Test
    public void testCheckAvailableExternalSupplyService() {
        supplyServicesFacade.addExternalService(supplyURl);

        String result = supplyServicesFacade.checkAvailableExternalSupplyService("TestCountry", "TestCity");
        assertEquals(supplyURl, result);

        String resultNotFound = supplyServicesFacade.checkAvailableExternalSupplyService("NonExistentCountry", "NonExistentCity");
//        assertEquals("-2", resultNotFound);
    }

    @Test
    public void testGetAllSupplyServices() {
        supplyServicesFacade.addExternalService(supplyURl);

        Map<String, ExternalSupplyService> allServices = supplyServicesFacade.getAllSupplyServices();
        assertEquals(1, allServices.size());
        assertTrue(allServices.containsKey(supplyURl));
    }

    @Test
    public void testCreateShiftingDetails() {
        supplyServicesFacade.addExternalService(supplyURl);

//        boolean result = supplyServicesFacade.createShiftingDetails(licensedDealerNumber, "User", "TestCountry", "TestCity", "TestAddress");
//        assertTrue(result);

        ExternalSupplyService service = supplyServicesFacade.getExternalSupplyServiceByURL(supplyURl);
        assertNotNull(service);
      //  assertTrue(service.createShiftingDetails("User", "TestCountry", "TestCity", "TestAddress"));
    }

    @Test
    public void testReset() {
        supplyServicesFacade.addExternalService(supplyURl);
        supplyServicesFacade.reset();

        assertTrue(supplyServicesFacade.getAllSupplyServices().isEmpty());
    }
}