package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.DiscountValueDTO;
import Util.ExceptionsEnum;
import Util.TestRuleDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ComposeCondDiscountTest {
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
        impl.addProductToStore(saarUserID, storeId,"weddingDress", 10, 5, "pink", "clothing");
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules1 = new ArrayList<>();
        rules1.add(rule1);
        rules1.add(rule2);
        List<String> logicalOperators1 = new ArrayList<>();
        logicalOperators1.add("AND");

        List<DiscountValueDTO> discountDetails1 = new ArrayList<>();
        discountDetails1.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails1.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators1 = new ArrayList<>();
        numericalOperators1.add("MAX");
        impl.addDiscountCondRuleToStore(rules1, logicalOperators1, discountDetails1, numericalOperators1, saarUserID, storeId);

        TestRuleDTO rule3 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule4 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules2 = new ArrayList<>();
        rules2.add(rule3);
        rules2.add(rule4);
        List<String> logicalOperators2 = new ArrayList<>();
        logicalOperators2.add("AND");

        List<DiscountValueDTO> discountDetails2 = new ArrayList<>();
        discountDetails2.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails2.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators2 = new ArrayList<>();
        numericalOperators2.add("ADD");
        impl.addDiscountCondRuleToStore(rules2, logicalOperators2, discountDetails2, numericalOperators2, saarUserID, storeId);


    }

    @Test
    public void successfulComposeTest() {
        assertTrue(impl.composeCurrentCondDiscountRules(0, 1, "OR", "ADD", saarUserID, storeId).isSuccess());

    }


    @Test
    public void logicalOperatorDontExist() {
        assertFalse(impl.composeCurrentCondDiscountRules(0, 1, "NOR", "ADD", saarUserID, storeId).isSuccess());
        assertEquals(impl.composeCurrentCondDiscountRules(0, 1, "NOR", "ADD", saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numericalOperatorDontExist() {
        assertFalse(impl.composeCurrentCondDiscountRules(0, 1, "OR", "MIN", saarUserID, storeId).isSuccess());
        assertEquals(impl.composeCurrentCondDiscountRules(0, 1, "OR", "MIN", saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void ruleNumDontExist() {
        assertFalse(impl.composeCurrentCondDiscountRules(2, 1, "OR", "ADD", saarUserID, storeId).isSuccess());
        assertEquals(impl.composeCurrentCondDiscountRules(2, 1, "OR", "ADD", saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleIndex.toString());
    }
}
