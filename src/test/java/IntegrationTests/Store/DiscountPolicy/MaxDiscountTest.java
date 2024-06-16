package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.MaxDiscount;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MaxDiscountTest {

    private List<ProductDTO> products;

    @BeforeEach
    public void setUp() {
        // Initialize products for testing
        products = new ArrayList<>();
        products.add(new ProductDTO("product1", 100, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", 200, 3, "desc2", "BOOKS"));
        products.add(new ProductDTO("product3", 300, 4, "desc3", "TOYS"));
    }

    @Test
    public void testCalcDiscount() {
        // Create a MaxDiscount with specific discount values
        DiscountValue discountValue1 = new SimpleDiscountValue(50, Category.TOYS, false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(20, Category.BOOKS, false, null);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = maxDiscount.calcDiscount(products);

        // Expected discount calculation:
        // TOYS category discount: (100 + 300) * 0.5 = 200
        // BOOKS category discount: 200 * 0.2 = 40
        // Maximum discount: max(200, 40) = 200
        assertEquals(200, totalDiscount);
    }

    @Test
    public void testCalcDiscountNoMatchingProducts() {
        // Create a MaxDiscount with discount values that won't match any products
        DiscountValue discountValue1 = new SimpleDiscountValue(50, Category.ELECTRONICS, false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(20, Category.FOOD, false, null);
        MaxDiscount maxDiscount = new MaxDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = maxDiscount.calcDiscount(products);

        // Expected discount calculation:
        // No products match 'ELECTRONICS' or 'FOOD', so total discount should be 0
        assertEquals(0, totalDiscount);
    }
}
