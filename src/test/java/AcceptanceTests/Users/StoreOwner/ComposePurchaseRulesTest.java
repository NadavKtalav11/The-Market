package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.ExceptionsEnum;
import Util.TestRuleDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComposePurchaseRulesTest {
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

        TestRuleDTO rule1 = new TestRuleDTO("Age", "Below", "ALCOHOL", null, "Alcohol cannot be sold to users below the age of 18", false, 18, null, null, null, null);
        TestRuleDTO rule2 = new TestRuleDTO("Time", "Above", "ALCOHOL", null, "Alcohol cannot be sold after 23:00", false, null, null, null, null, LocalTime.of(23, 0));
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        impl.addPurchaseRuleToStore(rules, List.of("AND"), saarUserID, storeId);

        TestRuleDTO rule3 = new TestRuleDTO("Amount", "Below", null, "Tomato", "Basket must contain less than 5kg of tomatoes", true, null, 5, null, null, null);
        List<TestRuleDTO> singleRule = new ArrayList<>();
        singleRule.add(rule3);
        impl.addPurchaseRuleToStore(singleRule, new ArrayList<>(), saarUserID, storeId);
    }

    @Test
    public void successfulComposeTest() {
        assertTrue(impl.composeCurrentPurchaseRules(0, 1, "AND", saarUserID, storeId).isSuccess());
    }

    @Test
    public void logicalOperatorDontExist() {
        assertFalse(impl.composeCurrentPurchaseRules(0, 1, "NOR", saarUserID, storeId).isSuccess());
        assertEquals(impl.composeCurrentPurchaseRules(0, 1, "NOR", saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void ruleNumDontExist() {
        assertFalse(impl.composeCurrentPurchaseRules(2, 1, "AND", saarUserID, storeId).isSuccess());
        assertEquals(impl.composeCurrentPurchaseRules(2, 1, "AND", saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleIndex.toString());
    }
}
