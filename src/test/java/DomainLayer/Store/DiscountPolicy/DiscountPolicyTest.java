package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import DomainLayer.Store.StoreDiscountPolicy.CondDiscount;
import DomainLayer.Store.StoreDiscountPolicy.Discount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DiscountPolicyTest {

    @Mock
    private Discount discount;

    @Mock
    private CondDiscount condDiscount;

    @Mock
    private Rule rule;

    @Mock
    private DiscountValue discountValue;

    private DiscountPolicy discountPolicy;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        discountPolicy = new DiscountPolicy();
    }

    @Test
    public void testCalcDiscountPolicy() {
        // Arrange
        UserDTO userDTO = new UserDTO(); // Assume UserDTO has a default constructor
        ProductDTO product1 = new ProductDTO("Product 1", 100, 2, "Description 1", "Category 1");
        ProductDTO product2 = new ProductDTO("Product 2", 200, 1, "Description 2", "Category 2");
        List<ProductDTO> basketProducts = Arrays.asList(product1, product2);

        // Mock the behavior of the discount objects
        when(discount.calcDiscount(basketProducts, userDTO)).thenReturn(50);
        when(condDiscount.calcDiscount(basketProducts, userDTO)).thenReturn(30);

        // Directly inject the mocks into the discountRules list
        discountPolicy.addRule(discount);
        discountPolicy.addRule(condDiscount);

        // Act
        int totalDiscount = discountPolicy.calcDiscountPolicy(userDTO, basketProducts);

        // Assert
        assertEquals(80, totalDiscount);
    }

    @Test
    public void testAddCondRule() {
        // Arrange
        List<Rule> rules = Arrays.asList(rule);
        List<String> logicalOperators = Arrays.asList();
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> numericalOperators = Arrays.asList();

        // Act
        discountPolicy.addCondRule(rules, logicalOperators, discDetails, numericalOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(CondDiscount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testAddSimple() {
        // Arrange
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> discountValueOperators = Arrays.asList();

        // Act
        discountPolicy.addSimple(discDetails, discountValueOperators);

        // Assert
        assertEquals(1, discountPolicy.getDiscountRules().size());
        assertEquals(Discount.class, discountPolicy.getDiscountRules().get(0).getClass());
    }

    @Test
    public void testRemoveRule() {
        // Arrange
        List<DiscountValue> discDetails = Arrays.asList(discountValue);
        List<String> discountValueOperators = Arrays.asList();
        discountPolicy.addSimple(discDetails, discountValueOperators);

        // Act
        discountPolicy.removeRule(0);

        // Assert
        assertEquals(0, discountPolicy.getDiscountRules().size());
    }
}
