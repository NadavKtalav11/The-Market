package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class AmountRule extends TestRule {
    private int quantity;

    public AmountRule(int quantity, String range, Category category, String productName, String description, boolean contains) {
        super(range, category, productName, description, contains);
        this.quantity = quantity;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (contains)
            return checkRange(range, getQuantity(products), quantity);
        else
            return !checkRange(range, getQuantity(products), quantity);
    }
}
