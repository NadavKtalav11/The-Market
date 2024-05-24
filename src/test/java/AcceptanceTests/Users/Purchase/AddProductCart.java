package AcceptanceTests.Users.Purchase;

import AcceptanceTests.BridgeService;
import AcceptanceTests.ProxyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AddProductCart {

    private BridgeService impl = new ProxyService();;


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulAdditionTest() {

    }

    @Test
    public void invalidProductNameTest() {

    }

    @Test
    public void outOfStockProductTest() {

    }

    @Test
    public void bigQuantityTest() {

    }

    @Test
    public void negQuantityTest() {

    }

    @Test
    public void purchasePolicyInvalidTest() {

    }
}
