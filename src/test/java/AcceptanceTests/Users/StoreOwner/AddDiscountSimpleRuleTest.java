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

public class AddDiscountSimpleRuleTest {
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
        //implement using addDiscountSimpleRuleToStore
        List<DiscountValueDTO> discountDetails1 = new ArrayList<>();
        discountDetails1.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails1.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators1 = new ArrayList<>();
        numericalOperators1.add("MAX");

        assertTrue(impl.addDiscountSimpleRuleToStore(discountDetails1, numericalOperators1, saarUserID, storeId).isSuccess());

        List<DiscountValueDTO> discountDetails2 = new ArrayList<>();
        discountDetails2.add(new DiscountValueDTO(10, "ELECTRONICS", false, null));
        List<String> numericalOperators2 = new ArrayList<>();

        assertTrue(impl.addDiscountSimpleRuleToStore(discountDetails2, numericalOperators2, saarUserID, storeId).isSuccess());
    }


    @Test
    public void numericalOperatorDontExist() {
        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MIN");

        assertFalse(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidOperator.toString());
    }


    @Test
    public void numOfDiscountValuesDontMatchNumericalDiscounts() {
        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.rulesNotMatchOpeators.toString());
    }

    @Test
    public void discountValueFieldsNull() {
        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, null, false, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }

    @Test
    public void discountValueFieldsNotMatch() {
        List<DiscountValueDTO> discountDetails = new ArrayList<>();
        discountDetails.add(new DiscountValueDTO(10, "CLOTHING", true, null));
        discountDetails.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators = new ArrayList<>();
        numericalOperators.add("MAX");

        assertFalse(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).isSuccess());
        assertEquals(impl.addDiscountSimpleRuleToStore(discountDetails, numericalOperators, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidDiscountValueParameters.toString());
    }
}
