package DomainLayer.Store.LogicalConditions;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import DomainLayer.Store.StorePurchasePolicy.*;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

class SimpleRuleTest {

    private UserDTO userDTO;
    private List<ProductDTO> products;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO("userId12345", "BestUser", "12/12/12", "Israel", "Bash", "Mesada", "Toy");
        products = new ArrayList<>();
    }

    @Test
    void testAlcoholRestrictionBelowAge18() {
        TestRuleDTO rule1 = new TestRuleDTO("Age", "Below", "ALCOHOL", null, "Alcohol cannot be sold to users below the age of 18", false, 18, null, null, null, null);
        SimpleRule simpleRule = new SimpleRule(rule1);

        userDTO.setBirthday("15/06/10"); // User is 13 years old
        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        assertFalse(simpleRule.checkRule(userDTO, products));

        userDTO.setBirthday("15/06/00"); // User is 23 years old
        assertTrue(simpleRule.checkRule(userDTO, products));
    }

    //TODO: Fix this test
    /*
    @Test
    void testAlcoholRestrictionAfter2300() {
        Clock after2300Clock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(after2300Clock);
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(PurchaseRulesRepository.ALCOHOL_RESTRICTION_AFTER_2300);

        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        assertFalse(rule.checkRule(userDTO, products));

        Clock before2300Clock = Clock.fixed(Instant.parse("2024-06-01T22:30:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(before2300Clock);
        rule = new SimpleRule<>(PurchaseRulesRepository.ALCOHOL_RESTRICTION_AFTER_2300);
        assertTrue(rule.checkRule(userDTO, products));
    }
     */

    @Test
    void testBasketContainsLessThan5KgTomatoes() {
        TestRuleDTO rule3 = new TestRuleDTO("Amount", "Below", null, "Tomatoes", "Basket must contain less than 5kg of tomatoes", true, null, 5, null, null, null);
        SimpleRule simpleRule = new SimpleRule(rule3);

        ProductDTO product = new ProductDTO("tomato", 5, 4, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(simpleRule.checkRule(userDTO, products));

        product.setQuantity(5);
        assertFalse(simpleRule.checkRule(userDTO, products));
    }

    @Test
    void testNoIceCreamInRoshHodesh() {
        Clock roshHodeshClock = Clock.fixed(Instant.parse("2024-06-01T10:00:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(roshHodeshClock);
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(PurchaseRulesRepository.NO_ICE_CREAM_IN_ROSH_HODESH);

        ProductDTO product = new ProductDTO("ice cream", 9, 1, "Dessert", "FOOD");
        products.add(product);

        assertFalse(rule.checkRule(userDTO, products));

        Clock notRoshHodeshClock = Clock.fixed(Instant.parse("2024-06-02T10:00:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(notRoshHodeshClock);
        rule = new SimpleRule<>(PurchaseRulesRepository.NO_ICE_CREAM_IN_ROSH_HODESH);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsAtLeast2Corns() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(PurchaseRulesRepository.BASKET_CONTAINS_AT_LEAST_2_CORNS);

        ProductDTO product = new ProductDTO("corn", 7, 1, "Vegetable", "FOOD");
        products.add(product);
        assertFalse(rule.checkRule(userDTO, products));

        product.setQuantity(2);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsEggplants() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(PurchaseRulesRepository.BASKET_CONTAINS_EGGPLANTS);

        assertFalse(rule.checkRule(userDTO, products));

        ProductDTO product = new ProductDTO("eggplants", 10, 1, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testIsHolidayEvening() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(PurchaseRulesRepository.IS_HOLIDAY_EVENING);

        // Test for the evening before Christmas
        Clock christmasEveClock = Clock.fixed(Instant.parse("2024-12-24T10:00:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(christmasEveClock);
        assertTrue(rule.checkRule(userDTO, products));

        // Test for a regular day
        Clock regularDayClock = Clock.fixed(Instant.parse("2024-12-26T10:00:00Z"), ZoneId.of("UTC"));
        PurchaseRulesRepository.setClock(regularDayClock);
        assertFalse(rule.checkRule(userDTO, products));
    }
}
