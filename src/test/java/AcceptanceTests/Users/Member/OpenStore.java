package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OpenStore {
    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulOpenStoreTest() {

    }

    @Test
    public void missingDetailsTest() {

    }

    @Test
    public void storeNameAlreadyExistTest() {

    }

}
