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
        mockCountries = Mockito.mock(HashSet.class);
        mockCities = Mockito.mock(HashSet.class);
        shiftingDetailsMock = Mockito.mock(ShiftingDetails.class);
        shiftIdAndDetails = Mockito.mock(HashSet.class);


        // Initialize the service with the mock data
        externalSupplyService = new ExternalSupplyService(12345, "MockService", mockCountries, mockCities);
    }

    @Test
    public void testGetLicensedDealerNumber() {
        assertEquals(12345, externalSupplyService.getLicensedDealerNumber());
    }

    @Test
    public void testCheckAreaAvailability_CountryNotAvailable() {
        Mockito.when(mockCountries.contains("MockCountry")).thenReturn(false);
        Mockito.when(mockCities.contains("MockCity")).thenReturn(true);

        assertFalse(externalSupplyService.checkAreaAvailability("MockCountry", "MockCity"));
    }

    @Test
    public void testCheckAreaAvailability_CityNotAvailable() {
        Mockito.when(mockCountries.contains("MockCountry")).thenReturn(true);
        Mockito.when(mockCities.contains("MockCity")).thenReturn(false);

        assertFalse(externalSupplyService.checkAreaAvailability("MockCountry", "MockCity"));
    }

    @Test
    public void testCheckAreaAvailability_AreaAvailable() {
        Mockito.when(mockCountries.contains("MockCountry")).thenReturn(true);
        Mockito.when(mockCities.contains("MockCity")).thenReturn(true);

        assertTrue(externalSupplyService.checkAreaAvailability("MockCountry", "MockCity"));
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
