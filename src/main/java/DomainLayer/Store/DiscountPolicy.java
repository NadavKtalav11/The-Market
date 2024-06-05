package DomainLayer.Store;

import java.util.ArrayList;
import java.util.List;

public class DiscountPolicy {
    private List<String> usersIdsToDiscount;
    private List<String> productNameToDiscount;
    private Discount discount;

    private final Object userIdLock;
    private final Object productNameLock;

    public DiscountPolicy()
    {
        this.usersIdsToDiscount = new ArrayList<>();
        this.productNameToDiscount = new ArrayList<>();
        this.discount = new Discount(0); //Default discount percentage is 0
        userIdLock = new Object();
        productNameLock = new Object();
    }

    public boolean checkDiscountPolicy(String userId, String productName)
    {
        //No requirement to discount policy, return true
        return true;
    }

}
