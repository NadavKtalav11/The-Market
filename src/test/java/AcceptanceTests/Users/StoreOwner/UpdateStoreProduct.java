package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class UpdateStoreProduct {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

    }

    @Test
    public void successfulUpdateTest() {

    }

    @Test
    public void productNotExistTest() {

    }


    @Test
    public void negQuantityTest() {

    }

    @Test
    public void emptyFieldTest() {

    }
}
