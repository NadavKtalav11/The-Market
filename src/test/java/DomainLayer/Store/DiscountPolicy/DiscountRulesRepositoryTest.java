package DomainLayer.Store.DiscountPolicy;

import DomainLayer.Store.StoreDiscountPolicy.DiscountRulesRepository;
import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DiscountRulesRepositoryTest {

    private Clock fixedClock;

    @BeforeEach
    public void setUp() {
        // Set a fixed clock for testing AFTER_2300_DISCOUNT rule
        Instant fixedInstant = LocalDate.now().atTime(23, 30).atZone(ZoneId.systemDefault()).toInstant();
        fixedClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
        DiscountRulesRepository.setClock(fixedClock);
    }

    @Test
    public void testBasketPriceGreaterThan100_True() {
        // Arrange
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO("Product 1", 50, 2, "Description 1", "Category 1"),
                new ProductDTO("Product 2", 60, 1, "Description 2", "Category 2")
        );

        // Act
        boolean result = DiscountRulesRepository.BASKET_PRICE_GREATER_THAN_100.getPredicate().test(new UserDTO(), products);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testBasketPriceGreaterThan100_False() {
        // Arrange
        List<ProductDTO> products = Arrays.asList(
                new ProductDTO("Product 1", 50, 1, "Description 1", "Category 1"),
                new ProductDTO("Product 2", 30, 1, "Description 2", "Category 2")
        );

        // Act
        boolean result = DiscountRulesRepository.BASKET_PRICE_GREATER_THAN_100.getPredicate().test(new UserDTO(), products);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testAfter2300Discount_True() {
        // Act (clock is set in @BeforeEach)
        boolean result = DiscountRulesRepository.AFTER_2300_DISCOUNT.getPredicate().test(new UserDTO(), Arrays.asList());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testAfter2300Discount_False() {
        // Set a different clock time (before 23:00)
        Instant fixedInstant = LocalDate.now().atTime(22, 30).atZone(ZoneId.systemDefault()).toInstant();
        Clock anotherClock = Clock.fixed(fixedInstant, ZoneId.systemDefault());
        DiscountRulesRepository.setClock(anotherClock);

        // Act
        boolean result = DiscountRulesRepository.AFTER_2300_DISCOUNT.getPredicate().test(new UserDTO(), Arrays.asList());

        // Assert
        assertFalse(result);
    }
}
