package AcceptanceTests.Users.StoreOwner;

import AcceptanceTests.BridgeToTests;
import AcceptanceTests.ProxyToTest;
import ServiceLayer.Response;
import Util.ExceptionsEnum;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AddPurchaseRuleTest {

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
    }

    @Test
    public void successfulAdditionTest() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> operators = new ArrayList<>();
        operators.add("AND");

        assertTrue(impl.addPurchaseRuleToStore(ruleNums, operators, saarUserID, storeId).isSuccess());

        List<Integer> singleRule = new ArrayList<>();
        singleRule.add(3);
        List<String> emptyOperators = new ArrayList<>();

        assertTrue(impl.addPurchaseRuleToStore(singleRule, emptyOperators, saarUserID, storeId).isSuccess());
    }

    @Test
    public void ruleNumDontExist() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(100);
        List<String> operators = new ArrayList<>();

        assertFalse(impl.addPurchaseRuleToStore(ruleNums, operators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addPurchaseRuleToStore(ruleNums, operators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleNumber.toString());
    }

    @Test
    public void operatorDontExist() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> operators = new ArrayList<>();
        operators.add("NOR");

        assertFalse(impl.addPurchaseRuleToStore(ruleNums, operators, storeId, saarUserID).isSuccess());
        assertEquals(impl.addPurchaseRuleToStore(ruleNums, operators, storeId, saarUserID).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }

    @Test
    public void numOfRuleDontMuchOperators() {
        List<Integer> ruleNums = new ArrayList<>();
        ruleNums.add(1);
        ruleNums.add(2);
        List<String> operators = new ArrayList<>();

        assertFalse(impl.addPurchaseRuleToStore(ruleNums, operators, storeId, saarUserID).isSuccess());
        assertEquals(impl.addPurchaseRuleToStore(ruleNums, operators, storeId, saarUserID).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }
}
