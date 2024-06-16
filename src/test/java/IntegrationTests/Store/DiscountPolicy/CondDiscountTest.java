package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.SimpleRule;
import DomainLayer.Store.StoreDiscountPolicy.CondDiscount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import Util.UserDTO;
import DomainLayer.Store.StoreDiscountPolicy.DiscountRulesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CondDiscountTest {

    private List<ProductDTO> products;
    private UserDTO user;

    @BeforeEach
    public void setUp() {
        // Initialize products for testing
        products = new ArrayList<>();
        products.add(new ProductDTO("product1", 100, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("bun", 10, 6, "desc2", "FOOD"));
        products.add(new ProductDTO("pasta", 10, 4, "desc3", "FOOD"));
        products.add(new ProductDTO("bread", 20, 3, "desc4", "FOOD"));

        // Initialize user for testing
        user = new UserDTO();
    }

    @Test
    public void testCalcDiscountWithValidRule() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(50, null, true, null));

        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        rules.add(new SimpleRule<>(DiscountRulesRepository.BASKET_CONTAINS_5_BUNS));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 50% discount: 145 * 0.5 = 70
        assertEquals(70, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithInvalidRule() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(50, null, true, null));

        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        rules.add(new SimpleRule<>(DiscountRulesRepository.BASKET_CONTAINS_5_BUNS));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Modify products to invalidate the rule
        products.get(1).setQuantity(4); // Reduce buns to less than 5

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // No discount should be applied because the rule is not met
        assertEquals(0, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithMultipleRules() {
        // Arrange
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(20, null, true, null));

        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        rules.add(new SimpleRule<>(DiscountRulesRepository.BASKET_CONTAINS_5_BUNS));
        rules.add(new SimpleRule<>(DiscountRulesRepository.BASKET_CONTAINS_3_PASTA));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();
        discountRuleOperators.add("AND");

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 20% discount: 140 * 0.2 = 28
        assertEquals(28, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithTimeBasedRule() {
        // Arrange
        // Set the clock to a specific time after 23:00
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2023, 6, 15, 23, 30).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DiscountRulesRepository.setClock(fixedClock);

        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(30, null, true, null));

        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        rules.add(new SimpleRule<>(DiscountRulesRepository.AFTER_2300_DISCOUNT));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // Expected discount calculation:
        // Total price of products: 100 + 10 + 10 + 20 = 140
        // 30% discount: 140 * 0.3 = 42
        assertEquals(42, totalDiscount);
    }

    @Test
    public void testCalcDiscountWithTimeBasedRuleBeforeTime() {
        // Arrange
        // Set the clock to a specific time before 23:00
        Clock fixedClock = Clock.fixed(LocalDateTime.of(2023, 6, 15, 22, 30).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DiscountRulesRepository.setClock(fixedClock);

        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(30, null, true, null));

        List<Rule<UserDTO, List<ProductDTO>>> rules = new ArrayList<>();
        rules.add(new SimpleRule<>(DiscountRulesRepository.AFTER_2300_DISCOUNT));

        List<String> discountValueOperators = new ArrayList<>();
        List<String> discountRuleOperators = new ArrayList<>();

        // Create CondDiscount
        CondDiscount condDiscount = new CondDiscount(discDetails, discountValueOperators, rules, discountRuleOperators);

        // Act
        int totalDiscount = condDiscount.calcDiscount(products, user);

        // Assert
        // No discount should be applied because the rule is not met (before 23:00)
        assertEquals(0, totalDiscount);
    }
}
