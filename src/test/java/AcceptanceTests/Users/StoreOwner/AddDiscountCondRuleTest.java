package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import Util.DiscountValueDTO;
import Util.ExceptionsEnum;
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
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertTrue(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());

    }

    @Test
    public void ruleNumDontExist() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(100);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleNumber.toString());
    }

    @Test
    public void logicalOperatorDontExist() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("NOR");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numericalOperatorDontExist() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MIN");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numOfRuleDontMatchLogicalOperators() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }

    @Test
    public void numOfDiscountValuesDontMatchNumericalDiscounts() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }

    @Test
    public void discountValueFieldsNull() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, null, false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }

    @Test
    public void discountValueFieldsNotMatch() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> logicalOperators = new ArrayList<>();
        logicalOperators.add("AND");

        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, null, true, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", true, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountCondRuleToStore(ruleNums, logicalOperators, discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }
}
