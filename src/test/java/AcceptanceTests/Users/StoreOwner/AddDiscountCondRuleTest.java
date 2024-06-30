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

public class AddDiscountCondRuleTest {
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
    }

    @Test
    public void successfulAdditionTest() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertTrue(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());

    }


    @Test
    public void logicalOperatorDontExist() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("NOR");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numericalOperatorDontExist() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MIN");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numOfRuleDontMatchLogicalOperators() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }

    @Test
    public void numOfDiscountValuesDontMatchNumericalDiscounts() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }

    @Test
    public void discountValueFieldsNull() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, null, false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }

    @Test
    public void discountValueFieldsNotMatch() {
        TestRuleDTO rule1 = new TestRuleDTO("Price", "Above", null, null, "Basket price is greater than 100 shekels", true, null, null, null, 100, null);
        TestRuleDTO rule2 = new TestRuleDTO("Amount", "Above", null, "bun", "Basket must contain more than 5 buns", true, null, 5, null, null, null);
        List<TestRuleDTO> rules = new ArrayList<>();
        rules.add(rule1);
        rules.add(rule2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, null, true, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", true, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(rules, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }
}
