package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Registering {

    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulRegistrationTest() {

    }

    @Test
    public void dupEmailTest() {

    }

    @Test
    public void incorrectEmailTest() {

    }
}
