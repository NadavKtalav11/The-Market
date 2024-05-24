package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeService;
import AcceptanceTests.ProxyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class InStoreSearch {
    private BridgeService impl = new ProxyService();;


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
