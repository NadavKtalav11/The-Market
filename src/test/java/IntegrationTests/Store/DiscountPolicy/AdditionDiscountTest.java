package IntegrationTests.Store.DiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.AdditionDiscount;
import DomainLayer.Store.StoreDiscountPolicy.DiscountValue;
import DomainLayer.Store.StoreDiscountPolicy.SimpleDiscountValue;
import Util.DiscountValueDTO;
import Util.ProductDTO;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class AdditionDiscountTest {

    private List<ProductDTO> products;

    @Before
    public void setUp() {
        // Initialize products for testing
        products = new ArrayList<>();
        products.add(new ProductDTO("product1", 100, 2, "desc1", "TOYS"));
        products.add(new ProductDTO("product2", 200, 3, "desc2", "BOOKS"));
        products.add(new ProductDTO("product3", 300, 4, "desc3", "TOYS"));
    }

    @Test
    public void testCalcDiscount() {
        // Create an AdditionDiscount with specific discount values
        DiscountValue discountValue1 = new SimpleDiscountValue(50, Category.TOYS,false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(20, null,true, null);
        AdditionDiscount additionDiscount = new AdditionDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = additionDiscount.calcDiscount(products);

       //expected discount calculation:
        // TOYS category discount: (100 + 300) * 0.5 = 200
        // Store discount: (100 + 200 + 300) * 0.2 = 120
        // Total discount: 120 + 200 = 320
        assertEquals("Total discount calculated", 320, totalDiscount);
    }

    @Test
    public void testCalcDiscountNoMatchingProducts() {
        // Create an AdditionDiscount with discount values that won't match any products
        DiscountValue discountValue1 = new SimpleDiscountValue(50, Category.ELECTRONICS,false, null);
        DiscountValue discountValue2 = new SimpleDiscountValue(20, Category.FOOD,false, null);
        AdditionDiscount additionDiscount = new AdditionDiscount(discountValue1, discountValue2);

        // Calculate discount
        int totalDiscount = additionDiscount.calcDiscount(products);

        // Expected discount calculation:
        // No products match 'Clothing' or 'Toys', so total discount should be 0
        assertEquals("Total discount calculated", 0, totalDiscount);
    }
}
