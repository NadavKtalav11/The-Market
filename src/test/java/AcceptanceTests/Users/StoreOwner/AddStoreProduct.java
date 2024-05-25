package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class AddStoreProduct {
    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulAdditionTest() {

    }

    @Test
    public void alreadyExistTest() {

    }


    @Test
    public void negQuantityTest() {

    }

    @Test
    public void emptyFieldTest() {

    }
}
