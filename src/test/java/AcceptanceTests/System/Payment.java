package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Payment {
    private static BridgeToTests impl;


    @BeforeAll
    public void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

    }
    @Test
    public void successfulPaymentTest() {

    }

    @Test
    public void invalidPaymentServiceTest() {

    }

    @Test
    public void noPaymentServiceTest() {

    }

    @Test
    public void invalidDetailsTest() {

    }
}
