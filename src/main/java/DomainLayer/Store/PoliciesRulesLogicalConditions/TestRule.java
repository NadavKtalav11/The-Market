package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public abstract class TestRule {
    protected final String range;
    protected final Category category;
    protected final String productName;
    protected final String description;

    public TestRule(String range, Category category, String productName, String description) {
        this.range = range;
        this.category = category;
        this.productName = productName;
        this.description = description;
    }

    public abstract boolean test(UserDTO user, List<ProductDTO> products);

    protected boolean isCategoryRule() {
        return category != null && productName == null;
    }

    protected boolean isProductsRule() {
        return category == null && productName != null;
    }

    protected boolean isRuleSatisfied(boolean conditionCheck, List<ProductDTO> products) {
        if (isCategoryRule()) {
            return conditionCheck && products.stream().anyMatch(p -> p.getCategoryStr().equals(category.toString()));
        } else if (isProductsRule()) {
            return conditionCheck && products.stream().anyMatch(p -> p.getName().equals(productName));
        }
        else {
            return conditionCheck;
        }
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
                throw new IllegalArgumentException("Invalid range: " + range);
        }
    }
}
