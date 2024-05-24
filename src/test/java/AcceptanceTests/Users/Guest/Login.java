package AcceptanceTests.Users.Guest;

import AcceptanceTests.BridgeService;
import AcceptanceTests.ProxyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class Login {

    private BridgeService impl = new ProxyService();;


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulLoginTest() {

    }

    @Test
    public void incorrectEmailTest() {

    }

    @Test
    public void incorrectPasswordTest() {

    }

    @Test
    public void alreadyLoggedInTest() {

    }
}
