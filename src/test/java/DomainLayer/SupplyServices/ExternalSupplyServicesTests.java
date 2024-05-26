package DomainLayer.SupplyServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class ExternalSupplyServicesTests {
    private ExternalSupplyService externalSupplyService;
    private HashSet<String> mockCountries;
    private HashSet<String> mockCities;
    private HashSet<String> shiftIdAndDetails;
    private ShiftingDetails shiftingDetailsMock; // Add mock for ShiftingDetails


    @BeforeEach
    public void setUp() {
        // Mock the sets of countries and cities
        HashSet<String> countries =new HashSet<>();
        HashSet<String> cities =new HashSet<>();

        countries.add("Israel");
        cities.add("Bash");
        shiftingDetailsMock = Mockito.mock(ShiftingDetails.class);
        shiftIdAndDetails = Mockito.mock(HashSet.class);


        // Initialize the service with the mock data
        externalSupplyService = new ExternalSupplyService(12345, "MockService", countries, cities);
    }

    @Test
    public void testGetLicensedDealerNumber() {
        assertEquals(12345, externalSupplyService.getLicensedDealerNumber());
    }

    @Test
    public void testCheckAreaAvailability_CountryNotAvailable() {


        assertFalse(externalSupplyService.checkAreaAvailability("France", "Bash"));
    }

    @Test
    public void testCheckAreaAvailability_CityNotAvailable() {

        assertFalse(externalSupplyService.checkAreaAvailability("Israel", "MockCity"));
    }

    @Test
    public void testAddCountries() {
        assertEquals(1,externalSupplyService.getCountries().size());
        HashSet<String> countries1 = new HashSet<>();
        countries1.add("Israel");
        countries1.add("France");
        externalSupplyService.addCountries(countries1);
        assertEquals(2,externalSupplyService.getCountries().size());

    }

    @Test
    public void testAddCities() {
        assertEquals(1,externalSupplyService.getCities().size());
        HashSet<String> cities1 = new HashSet<>();
        cities1.add("Ashdod");
        cities1.add("Tel aviv");
        externalSupplyService.addCities(cities1);
        assertEquals(3,externalSupplyService.getCities().size());

    }

    @Test
    public void testCheckAreaAvailability_AreaAvailable() {


        assertTrue(externalSupplyService.checkAreaAvailability("Israel", "Bash"));
    }

    @Test
    public void testCreateShiftingDetails() {
        // Ensure that the initial size of shiftIdAndDetails is zero
        assertEquals(0, externalSupplyService.getShiftIdAndDetails().size());

        boolean result = externalSupplyService.createShiftingDetails("User1", "MockCountry", "MockCity", "MockAddress");
        assertTrue(result);
       assertEquals(1, externalSupplyService.getShiftIdAndDetails().size());
        assertNotNull(externalSupplyService.getShiftIdAndDetails().get(1));
    }
}
