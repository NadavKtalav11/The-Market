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
    protected final boolean contains;

    public TestRule(String range, Category category, String productName, String description, boolean contains) {
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
            return (int) products.stream().filter(p -> p.getCategoryStr().equals(category.toString())).count();
        } else if (isProductsRule()) {
            return (int) products.stream().filter(p -> p.getName().equals(productName)).count();
        }
        throw new IllegalArgumentException("Invalid rule type");
    }

    protected boolean isRuleSatisfied(List<ProductDTO> products, int quantity) {
        if (contains) {
            if(quantity > 0)
            {
                //switch case on range
                return switch (range) {
                    case "Above" -> getQuantity(products) > quantity;
                    case "Below" -> getQuantity(products) < quantity;
                    case "`Exactly" -> getQuantity(products) == quantity;
                    default -> throw new IllegalArgumentException("Invalid range: " + range);
                };
            }
            else
            {
                return getQuantity(products) > quantity;
            }
        }
        else {
            if(quantity > 0)
            {
                //switch case on range
                return switch (range) {
                    case "Above" -> getQuantity(products) <= quantity;
                    case "Below" -> getQuantity(products) >= quantity;
                    case "`Exactly" -> getQuantity(products) != quantity;
                    default -> throw new IllegalArgumentException("Invalid range: " + range);
                };
            }
            else
            {
                return getQuantity(products) == quantity;
            }
        }
    }

    public String getDescription() {
        return description;
    }
}
