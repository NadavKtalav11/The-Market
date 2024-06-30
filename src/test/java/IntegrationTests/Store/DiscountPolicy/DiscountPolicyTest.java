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
    int percentage1;
    int percentage2;
    int percentage3;
    int product1Price;
    int product2Price;

    @BeforeEach
    public void setUp() {
        discountPolicy = new DiscountPolicy();
        percentage1 = 50;
        percentage2 = 30;
        percentage3 = 10;
        product1Price = 100;
        product2Price = 50;

        // Add some sample rules for testing
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage1, null, true, null));
        List<String> discountValueOperators = new ArrayList<>();
        discountPolicy.addSimple(discDetails, discountValueOperators);
    }

    @Test
    public void testAddRule() {
        assertEquals(1, discountPolicy.getDiscountRules().size());

        // Add another rule
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage2, null, true, null));
        discDetails.add(new SimpleDiscountValue(percentage3, Category.FOOD, false, null));

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
        products.add(new ProductDTO("product1", product1Price, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", product2Price, 5, "desc2", "TOYS"));

        // Calculate discount
        int totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        int expectedDiscount = (product1Price + product2Price) * percentage1 / 100;
        assertEquals(expectedDiscount, totalDiscount); //50% discount on all store products

        // Add another rule that matches the products
        List<DiscountValue> discDetails = new ArrayList<>();
        discDetails.add(new SimpleDiscountValue(percentage3, Category.TOYS, false, null));
        discountPolicy.addSimple(discDetails, new ArrayList<>());

        // Calculate discount again
        totalDiscount = discountPolicy.calcDiscountPolicy(user, products);

        int expectedDiscount2 = (product1Price + product2Price) * percentage1 / 100 + (product1Price + product2Price) * percentage3 / 100;
        assertEquals(expectedDiscount2, totalDiscount); //50% discount on all store products + 10% discount on TOYS category
    }
}
