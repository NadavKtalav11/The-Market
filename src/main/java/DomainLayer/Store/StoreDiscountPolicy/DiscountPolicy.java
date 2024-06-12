package DomainLayer.Store.StoreDiscountPolicy;

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
        //todo: Nitzan+Tomer fix below two lines (those are temporary lines)
        DiscountValue discountValue = new SimpleDiscountValue(0, null, true, null);//Default discount percentage is 0
        this.discount = new Discount(new ArrayList<>(List.of(discountValue)), null);
        userIdLock = new Object();
        productNameLock = new Object();
    }

    public boolean checkDiscountPolicy(String userId, String productName)
    {
        //No requirement to discount policy, return true
        return true;
    }

}
