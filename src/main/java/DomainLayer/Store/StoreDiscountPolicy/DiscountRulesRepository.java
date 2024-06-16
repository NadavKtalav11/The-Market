package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.TestRule;
import Util.ProductDTO;
import Util.UserDTO;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;

import static Util.ExceptionsEnum.InvalidRuleNumber;

public enum DiscountRulesRepository implements TestRule<UserDTO, List<ProductDTO>> {

    BASKET_PRICE_GREATER_THAN_100(1, "Basket price is greater than 100 shekels", (userDTO, products) -> {
        double totalPrice = 0;
        for(ProductDTO product: products) {
            totalPrice += product.getPrice() * product.getQuantity();
        }
        return totalPrice > 100;
    }),

    BASKET_CONTAINS_5_BUNS(2, "Basket must contain more than 5 buns", (userDTO, products) -> {
        int quantity = 0;
        for(ProductDTO product: products) {
            if(product.getName().equals("bun")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity > 5;
    }),

    BASKET_CONTAINS_3_PASTA(3, "Basket must contain more than 3 pasta", (userDTO, products) -> {
        int quantity = 0;
        for(ProductDTO product: products) {
            if(product.getName().equals("pasta")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity > 3;
    }),

    BASKET_CONTAINS_2_BREADS(4, "Basket must contain more than 2 breads", (userDTO, products) -> {
        int quantity = 0;
        for(ProductDTO product: products) {
            if(product.getName().equals("bread")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity > 2;
    }),

    BASKET_CONTAINS_3_COTTAGE(5, "Basket must contain more than 3 cottage", (userDTO, products) -> {
        int quantity = 0;
        for(ProductDTO product: products) {
            if(product.getName().equals("cottage")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity > 3;
    }),

    BASKET_CONTAINS_2_YOGURT(6, "Basket must contain more than 2 yogurt", (userDTO, products) -> {
        int quantity = 0;
        for(ProductDTO product: products) {
            if(product.getName().equals("yogurt")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity > 2;
    }),

    AFTER_2300_DISCOUNT(7, "Discount after 23:00", (userDTO, products) -> {
        LocalTime time = LocalTime.now(getClock());
        LocalTime targetTime = LocalTime.of(23, 0);
        return time.isAfter(targetTime);
    });

    private final int ruleNumber;
    private final String description;
    private final BiPredicate<UserDTO, List<ProductDTO>> predicate;
    private static final ThreadLocal<Clock> clock = ThreadLocal.withInitial(Clock::systemDefaultZone); // Default clock

    DiscountRulesRepository(int ruleNumber, String description, BiPredicate<UserDTO, List<ProductDTO>> predicate) {
        this.ruleNumber = ruleNumber;
        this.description = description;
        this.predicate = predicate;
    }

    public int getRuleNumber() {
        return ruleNumber;
    }

    public String getDescription() {
        return description;
    }

    public static Map<Integer, String> getAllRules() {
        Map<Integer, String> rules = new HashMap<>();
        for (DiscountRulesRepository rule : values()) {
            rules.put(rule.getRuleNumber(), rule.getDescription());
        }
        return rules;
    }

    public static DiscountRulesRepository getByRuleNumber(int ruleNumber) {
        for (DiscountRulesRepository rule : values()) {
            if (rule.getRuleNumber() == ruleNumber) {
                return rule;
            }
        }
        throw new IllegalArgumentException(InvalidRuleNumber.toString());
    }

    @Override
    public BiPredicate<UserDTO, List<ProductDTO>> getPredicate() {
        return predicate;
    }

    public static void setClock(Clock newClock) {
        clock.set(newClock);
    }

    public static Clock getClock() {
        return clock.get();
    }
}
