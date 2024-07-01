package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class AmountRule extends TestRule {
    private int quantity;
    protected final Object quantityLock;

    public AmountRule(int quantity, String range, Category category, String productName, String description, boolean contains) {
        super(range, category, productName, description, contains);
        this.quantity = quantity;
        this.quantityLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (getContains())
            return checkRange(getRange(), getQuantity(products), getQuantity());
        else
            return !checkRange(getRange(), getQuantity(products), getQuantity());
    }

    private int getQuantity(){
        synchronized (quantityLock) {
            return quantity;
        }
    }
}
