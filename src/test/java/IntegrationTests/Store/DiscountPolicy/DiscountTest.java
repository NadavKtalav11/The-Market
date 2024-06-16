package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.AdditionDiscount;
import DomainLayer.Store.StoreDiscountPolicy.Discount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiscountTest {

    private Discount discount;

    @BeforeEach
    public void setUp() {
        // Create a Discount instance with initial discount values
        List<DiscountValue> discountValues = new ArrayList<>();
        discountValues.add(new SimpleDiscountValue(20, Category.fromString( "FOOD"), false, null)); // 20% discount
        discountValues.add(new SimpleDiscountValue(50, Category.fromString( "FOOD"), false, null));

        List<String> operators = new ArrayList<>();
        operators.add("ADDITION"); // Apply addition operator

        discount = new Discount(discountValues, operators);
    }

    @Test
    public void testSetDiscountValue_Addition() {
        // Verify that the discount value is correctly set with addition operator
        DiscountValue discountValue = discount.getDiscountValue();
        assertEquals(AdditionDiscount.class, discountValue.getClass());
    }

    @Test
    public void testCalcDiscount() {
        // Create a basket of products
        List<ProductDTO> basketProducts = new ArrayList<>();
        basketProducts.add(new ProductDTO("Milk", 100, 2, "Good milk", "FOOD")); // Price: $100
        basketProducts.add(new ProductDTO("Cheese", 200, 2, "Good cheese", "FOOD")); // Price: $200

        // Create a userDTO (not used in this simple example)
        UserDTO userDTO = new UserDTO("Moshe");

        // Calculate discount
        int discountAmount = discount.calcDiscount(basketProducts, userDTO);

        // Expected discount: 20% of (100 + 200) + $50 = $110
        assertEquals(210, discountAmount);
    }

    @Test
    public void testGetDiscountValue() {
        // Verify that the correct discount value object is returned
        DiscountValue discountValue = discount.getDiscountValue();
        assertEquals(AdditionDiscount.class, discountValue.getClass());
    }
}
