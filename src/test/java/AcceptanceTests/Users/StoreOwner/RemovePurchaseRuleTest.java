package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RemovePurchaseRuleTest {

    private static BridgeToTests impl;
    static String saarUserID;
    static String storeId;

    @BeforeAll
    public static void setUp() {
        impl = new ProxyToTest("Real");
        saarUserID = impl.enterMarketSystem().getData();
        impl.register(saarUserID,"saar", "10/04/84", "Israel", "Jerusalem", "Yehuda halevi 18", "saar", "Fadidaa1");
        impl.login(saarUserID, "saar", "Fadidaa1");
        storeId = impl.openStore(saarUserID, "alona", "shopping").getData();
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "clothes");
        impl.addPurchaseRuleToStore(List.of(1, 2), List.of("AND"), saarUserID, storeId);
        impl.addPurchaseRuleToStore(List.of(3), new ArrayList<>(), saarUserID, storeId);
    }

    @Test
    public void successfulRemoveTest() {
        //remove rule 1
        assertTrue(impl.removePurchaseRuleFromStore(0, saarUserID, storeId).isSuccess());
        //remove rule 2
        assertTrue(impl.removePurchaseRuleFromStore(0, saarUserID, storeId).isSuccess());
    }

    @Test
    public void ruleNumDontExist() {
        assertFalse(impl.removePurchaseRuleFromStore(100, saarUserID, storeId).isSuccess());
        assertEquals(impl.removePurchaseRuleFromStore(100, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleIndex.toString());
    }
}
