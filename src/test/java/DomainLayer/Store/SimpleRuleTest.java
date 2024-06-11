package DomainLayer.Store;

import Util.ProductDTO;
import Util.UserDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

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
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.ALCOHOL_RESTRICTION_BELOW_AGE_18);

        userDTO.setBirthday("15/06/10"); // User is 13 years old
        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        assertFalse(rule.checkRule(userDTO, products));

        userDTO.setBirthday("15/06/00"); // User is 23 years old
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testAlcoholRestrictionAfter2300() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.ALCOHOL_RESTRICTION_AFTER_2300);

        ProductDTO product = new ProductDTO("Beer", 10, 1, "Alcohol", "ALCOHOL");
        products.add(product);

        // Test for time after 23:00
        Clock after2300Clock = Clock.fixed(Instant.parse("2024-06-01T23:30:00Z"), ZoneId.of("UTC"));
        LocalTime now = LocalTime.now(after2300Clock);
        assertFalse(rule.checkRule(userDTO, products));

        // Test for time before 23:00
        Clock before2300Clock = Clock.fixed(Instant.parse("2024-06-01T22:30:00Z"), ZoneId.of("UTC"));
        now = LocalTime.now(before2300Clock);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsLessThan5KgTomatoes() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.BASKET_CONTAINS_LESS_THAN_5KG_TOMATOS);

        ProductDTO product = new ProductDTO("tomato", 5, 4, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(rule.checkRule(userDTO, products));

        product.setQuantity(5);
        assertFalse(rule.checkRule(userDTO, products));
    }

    @Test
    void testNoIceCreamInRoshHodesh() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.NO_ICE_CREAM_IN_ROSH_HODESH);

        ProductDTO product = new ProductDTO("ice cream", 9, 1, "Dessert", "FOOD");
        products.add(product);

        // Test for Rosh Hodesh (1st day of the month)
        Clock roshHodeshClock = Clock.fixed(Instant.parse("2024-06-01T10:00:00Z"), ZoneId.of("UTC"));
        LocalDate today = LocalDate.now(roshHodeshClock);
        assertFalse(rule.checkRule(userDTO, products));

        // Test for not Rosh Hodesh (2nd day of the month)
        Clock notRoshHodeshClock = Clock.fixed(Instant.parse("2024-06-02T10:00:00Z"), ZoneId.of("UTC"));
        today = LocalDate.now(notRoshHodeshClock);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsAtLeast2Corns() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.BASKET_CONTAINS_AT_LEAST_2_CORNS);

        ProductDTO product = new ProductDTO("corn", 7, 1, "Vegetable", "FOOD");
        products.add(product);
        assertFalse(rule.checkRule(userDTO, products));

        product.setQuantity(2);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testBasketContainsEggplants() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.BASKET_CONTAINS_EGGPLANTS);

        assertFalse(rule.checkRule(userDTO, products));

        ProductDTO product = new ProductDTO("eggplants", 10, 1, "Vegetable", "FOOD");
        products.add(product);
        assertTrue(rule.checkRule(userDTO, products));
    }

    @Test
    void testIsHolidayEvening() {
        SimpleRule<UserDTO, List<ProductDTO>> rule = new SimpleRule<>(RulesRepository.IS_HOLIDAY_EVENING);

        // Test for the evening before Christmas
        Clock christmasEveClock = Clock.fixed(Instant.parse("2024-12-24T10:00:00Z"), ZoneId.of("UTC"));
        LocalDate today = LocalDate.now(christmasEveClock);
        assertTrue(rule.checkRule(userDTO, products));

        // Test for a regular day
        Clock regularDayClock = Clock.fixed(Instant.parse("2024-12-26T10:00:00Z"), ZoneId.of("UTC"));
        today = LocalDate.now(regularDayClock);
        assertFalse(rule.checkRule(userDTO, products));
    }
}
