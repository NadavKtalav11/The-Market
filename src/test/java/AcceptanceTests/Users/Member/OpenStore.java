package AcceptanceTests.Users.Member;

import AcceptanceTests.BridgeService;
import AcceptanceTests.ProxyService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class OpenStore {
    private BridgeService impl = new ProxyService();;


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
