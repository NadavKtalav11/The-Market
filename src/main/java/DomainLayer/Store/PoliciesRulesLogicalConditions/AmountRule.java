package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class AmountRule extends TestRule {
    private int quantity;

    public AmountRule(int quantity, boolean isAbove, Category category, boolean isStoreDiscount, String productName, String description) {
        super(isAbove, category, isStoreDiscount, productName, description);
        this.quantity = quantity;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        long productCount = products.stream()
                .filter(p -> p.getName().equals(productName))
                .count();
        boolean quantityCheck = isAbove ? productCount >= quantity : productCount <= quantity;

        return isRuleSatisfied(quantityCheck, products);
    }
}
