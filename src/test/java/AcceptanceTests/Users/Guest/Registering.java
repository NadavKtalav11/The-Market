package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeService;
import AcceptanceTests.ProxyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Registering {

    private BridgeService impl = new ProxyService();;


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
