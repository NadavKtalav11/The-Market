package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.UserDTO;

import java.time.Clock;
import java.util.List;

public abstract class TestRule {
    protected final String range;
    protected final Category category;
    protected final String productName;
    protected final String description;
    protected final Boolean contains;
    private static final ThreadLocal<Clock> clock = ThreadLocal.withInitial(Clock::systemDefaultZone); // Default clock

    public TestRule(String range, Category category, String productName, String description, Boolean contains) {
        this.range = range;
        this.category = category;
        this.productName = productName;
        this.description = description;
        this.contains = contains;
    }

    public abstract boolean test(UserDTO user, List<ProductDTO> products);

    protected boolean isCategoryRule() {
        return category != null && productName == null;
    }

    protected boolean isProductsRule() {
        return category == null && productName != null;
    }

    protected int getQuantity(List<ProductDTO> products) {
        if (isCategoryRule()) {
            return products.stream().filter(p -> p.getCategoryStr().equals(category.toString())).mapToInt(ProductDTO::getQuantity).sum();
        } else if (isProductsRule()) {
                //loop through products and sum the quantity of the product with the given name
            return products.stream().filter(p -> p.getName().equals(productName)).mapToInt(ProductDTO::getQuantity).sum();
        }
        throw new IllegalArgumentException(ExceptionsEnum.InvalidRuleType.toString());
    }

    public String getDescription() {
        return description;
    }

    public boolean checkRange(String range, double actual, double expected)
    {
        switch (range) {
            case "Above":
                return actual > expected;
            case "Below":
                return actual < expected;
            case "Exact":
                return actual == expected;
            default:
                throw new IllegalArgumentException(ExceptionsEnum.InvalidRangeType.toString());
        }
    }

    public static void setClock(Clock newClock) {
        clock.set(newClock);
    }

    public static Clock getClock() {
        return clock.get();
    }

    protected String getRange() {
        return range;
    }

    protected Category getCategory() {
        return category;
    }

    protected String getProductName() {
        return productName;
    }

    protected Boolean getContains() {
        return contains;
    }
}
