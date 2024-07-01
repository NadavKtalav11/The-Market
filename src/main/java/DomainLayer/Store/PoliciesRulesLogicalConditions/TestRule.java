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
    protected final boolean contains;
    private static final ThreadLocal<Clock> clock = ThreadLocal.withInitial(Clock::systemDefaultZone); // Default clock
    protected final Object rangeLock;
    protected final Object categoryLock;
    protected final Object productNameLock;
    protected final Object descriptionLock;
    protected final Object containsLock;
    private static final Object clockLock = new Object();

    public TestRule(String range, Category category, String productName, String description, boolean contains) {
        this.range = range;
        this.category = category;
        this.productName = productName;
        this.description = description;
        this.contains = contains;
        this.rangeLock = new Object();
        this.categoryLock = new Object();
        this.productNameLock = new Object();
        this.descriptionLock = new Object();
        this.containsLock = new Object();
    }

    public abstract boolean test(UserDTO user, List<ProductDTO> products);

    protected boolean isCategoryRule() {
        synchronized (categoryLock) {
            synchronized (productNameLock) {
                return category != null && productName == null;
            }
        }
    }

    protected boolean isProductsRule() {
        synchronized (categoryLock) {
            synchronized (productNameLock) {
                return category == null && productName != null;
            }
        }
    }

    protected int getQuantity(List<ProductDTO> products) {
        if (isCategoryRule()) {
            synchronized (categoryLock) {
                return products.stream().filter(p -> p.getCategoryStr().equals(category.toString())).mapToInt(ProductDTO::getQuantity).sum();
            }
        } else if (isProductsRule()) {
            synchronized (productNameLock) {
                //loop through products and sum the quantity of the product with the given name
                return products.stream().filter(p -> p.getName().equals(productName)).mapToInt(ProductDTO::getQuantity).sum();
            }
        }
        throw new IllegalArgumentException(ExceptionsEnum.InvalidRuleType.toString());
    }

    public String getDescription() {
        synchronized (descriptionLock) {
            return description;
        }
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
        synchronized (clockLock) {
            clock.set(newClock);
        }

    }

    public static Clock getClock() {
        synchronized (clockLock) {
            return clock.get();
        }
    }

    protected String getRange() {
        synchronized (rangeLock) {
            return range;
        }
    }

    protected Category getCategory() {
        synchronized (categoryLock) {
            return category;
        }
    }

    protected String getProductName() {
        synchronized (productNameLock) {
            return productName;
        }
    }

    protected boolean getContains() {
        synchronized (containsLock) {
            return contains;
        }
    }
}
