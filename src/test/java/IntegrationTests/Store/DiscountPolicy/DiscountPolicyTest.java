package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.DiscountPolicy;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import Util.UserDTO;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiscountPolicyTest {

    private DiscountPolicy discountPolicy;

    @BeforeEach
    public void setUp() {
        discountPolicy = new DiscountPolicy();

        // Add some sample rules for testing
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(50, null, true, null));
        List<String> discountValueOperators = new ArrayList<>();
        discountPolicy.addSimple(discDetails, discountValueOperators);
    }

    @Test
    public void testAddRule() {
        assertEquals(1, discountPolicy.getDiscountRules().size());

        // Add another rule
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(30, null, true, null));
        discDetails.add(new SimpleDiscountValue(10, Category.FOOD, false, null));

        List<String> discountValueOperators = new ArrayList<>();
        discountValueOperators.add("AND");
        discountPolicy.addSimple(discDetails, discountValueOperators);

        assertEquals(2, discountPolicy.getDiscountRules().size());
    }

    @Test
    public void testRemoveRule() {
        assertEquals(1, discountPolicy.getDiscountRules().size());

        // Remove the existing rule
        discountPolicy.removeRule(0);

        assertEquals(0, discountPolicy.getDiscountRules().size());
    }

    @Test
    public void testCalcDiscountPolicy() {
        // Create a UserDTO
        UserDTO user = new UserDTO();
        user.setUserId("123");

        // Create some ProductDTOs
        List<ProductDTO> products = new ArrayList<>();
        products.add(new ProductDTO("product1", 100, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", 50, 5, "desc2", "TOYS"));

        // Calculate discount
        int totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        assertEquals(75, totalDiscount); //50% discount on all store products

        // Add another rule that matches the products
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(10, Category.TOYS, false, null));
        discountPolicy.addSimple(discDetails, new ArrayList<>());

        // Calculate discount again
        totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        assertEquals(90, totalDiscount); //50% discount on all store products + 10% discount on TOYS category
    }
}
