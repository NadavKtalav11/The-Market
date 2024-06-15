package AcceptanceTests.System;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Provision {
    private static BridgeToTests impl;


    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        //Do what you need

    }

    @Test
    public void successfulTransferTest() {

    }

    @Test
    public void invalidExternalServiceTest() {

    }

    @Test
    public void noAvailableServiceTest() {

    }

    @Test
    public void invalidDetailsTest() {

    }

}
