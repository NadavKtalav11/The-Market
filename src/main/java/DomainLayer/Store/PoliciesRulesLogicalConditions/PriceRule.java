package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class PriceRule extends TestRule {
    private int price;

    public PriceRule(int price, boolean isAbove, Category category, String productName, String description) {
        super(isAbove, category, productName, description);
        this.price = price;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        double totalPrice = products.stream().mapToDouble(ProductDTO::getPrice).sum();
        boolean priceCheck = isAbove ? totalPrice >= price : totalPrice <= price;

        return isRuleSatisfied(priceCheck, products);
    }
}

