package DomainLayer.Store;

import java.util.ArrayList;
import java.util.List;

public class DiscountPolicy {
    private List<Integer> usersIdsToDiscount;
    private List<String> productNameToDiscount;
    private Discount discount;

    public DiscountPolicy()
    {
        this.usersIdsToDiscount = new ArrayList<>();
        this.productNameToDiscount = new ArrayList<>();
        this.discount = new Discount(0); //Default discount percentage is 0
    }

    public boolean checkDiscountPolicy(int userId, String productName)
    {
        //No requirement to discount policy, return true
        return true;
    }

}
