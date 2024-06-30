package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public abstract class TestRule {
    protected final boolean isAbove;
    protected final Category category;
    protected final boolean isStoreDiscount;
    protected final List<String> productsNames;
    protected final String description;

    public TestRule(boolean isAbove, Category category, boolean isStoreDiscount, List<String> productsNames, String description) {
        this.isAbove = isAbove;
        this.category = category;
        this.isStoreDiscount = isStoreDiscount;
        this.productsNames = productsNames;
        this.description = description;
    }

    public abstract boolean test(UserDTO user, List<ProductDTO> products);

    protected boolean isCategoryRule() {
        return category != null && productsNames == null && !isStoreDiscount;
    }

    protected boolean isProductsRule() {
        return category == null && productsNames != null && !isStoreDiscount;
    }

    protected boolean isStoreRule() {
        return category == null && productsNames == null && isStoreDiscount;
    }

    protected boolean isRuleSatisfied(boolean conditionCheck, List<ProductDTO> products) {
        if (isCategoryRule()) {
            return conditionCheck && products.stream().anyMatch(p -> p.getCategoryStr().equals(category.toString()));
        } else if (isProductsRule()) {
            return conditionCheck && products.stream().anyMatch(p -> productsNames.contains(p.getName()));
        } else if (isStoreRule()) {
            return conditionCheck;
        }

        return false;
    }

    public String getDescription() {
        return description;
    }
}
