package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;

import java.util.List;

public abstract class CompositeNumericalDiscount implements DiscountValue{
    protected DiscountValue discountValue1;
    protected DiscountValue discountValue2;

    public CompositeNumericalDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        this.discountValue1 = discountValue1;
        this.discountValue2 = discountValue2;
    }

    public abstract int calcDiscount();
}
