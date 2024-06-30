package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public abstract class TestRule {
    protected final boolean isAbove;
    protected final Category category;
    protected final String productName;
    protected final String description;

    public TestRule(boolean isAbove, Category category, String productName, String description) {
        this.isAbove = isAbove;
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

        return false;
    }

    public String getDescription() {
        return description;
    }
}
