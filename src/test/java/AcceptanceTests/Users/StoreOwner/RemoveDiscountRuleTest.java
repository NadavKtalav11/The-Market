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

public class RemoveDiscountRuleTest {
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

        List<DiscountValueDTO> discountDetails1 = new ArrayList<>();
        discountDetails1.add(new DiscountValueDTO(10, "CLOTHING", false, null));
        discountDetails1.add(new DiscountValueDTO(20, "CLOTHING", false, null));
        List<String> numericalOperators1 = new ArrayList<>();
        numericalOperators1.add("MAX");
        impl.addDiscountSimpleRuleToStore(discountDetails1, numericalOperators1, saarUserID, storeId);

        List<DiscountValueDTO> discountDetails2 = new ArrayList<>();
        discountDetails2.add(new DiscountValueDTO(10, "ELECTRONICS", false, null));
        List<String> numericalOperators2 = new ArrayList<>();
        impl.addDiscountSimpleRuleToStore(discountDetails2, numericalOperators2, saarUserID, storeId);
    }

    @Test
    public void successfulRemoveTest() {
        //remove rule 1
        assertTrue(impl.removeDiscountRuleFromStore(0, saarUserID, storeId).isSuccess());
        //remove rule 2
        assertTrue(impl.removeDiscountRuleFromStore(0, saarUserID, storeId).isSuccess());
    }

    @Test
    public void ruleNumDontExist() {
        assertFalse(impl.removeDiscountRuleFromStore(100, saarUserID, storeId).isSuccess());
        assertEquals(impl.removeDiscountRuleFromStore(100, saarUserID, storeId).getDescription(), ExceptionsEnum.InvalidRuleIndex.toString());
    }
}
