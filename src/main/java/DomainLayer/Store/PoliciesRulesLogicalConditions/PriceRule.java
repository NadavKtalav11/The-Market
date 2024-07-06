package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class PriceRule extends TestRule {
    private int price;
    protected final Object priceLock;

    public PriceRule(int price, String range, Category category, String productName, String description, boolean contains) {
        super(range, category, productName, description, contains);
        this.price = price;
        this.priceLock = new Object();
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        //calculate the total price of the products
        int totalPrice = 0;
        for(ProductDTO product : products){
            totalPrice += product.getPrice();
        }

        return checkRange(getRange(), totalPrice, getPrice());
    }

    private int getPrice(){
        synchronized (priceLock) {
            return price;
        }
    }
}

