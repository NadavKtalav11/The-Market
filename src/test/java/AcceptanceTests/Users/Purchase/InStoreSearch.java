package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InStoreSearch {
    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulSearchNoFiltersTest() {

    }

    @Test
    public void successfulSearchWithFiltersTest() {

    }

    @Test
    public void productNotExistTest() {

    }

    @Test
    public void categoryNotExistTest() {

    }

    @Test
    public void negativePriceRangeTest() {

    }

    @Test
    public void productRatingInvalidTest() {

    }

}
