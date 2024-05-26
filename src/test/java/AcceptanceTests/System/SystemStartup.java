package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SystemStartup {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

    }

    @Test
    public void successfulInitTest() {

    }

    @Test
    public void noSupplyServiceTest() {

    }


    @Test
    public void noPaymentServiceTest() {

    }
}
