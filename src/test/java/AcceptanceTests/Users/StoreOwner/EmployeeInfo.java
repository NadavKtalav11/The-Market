package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class EmployeeInfo {
    private BridgeToTests impl = new ProxyToTest("Real");


    @BeforeAll
    public void setUp() {

    }

    @Test
    public void successfulRequestTest() {

    }

}
