package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;

import java.util.List;

public abstract class CompositeNumericalDiscount implements DiscountValue{
    protected DiscountValue discountValue1;
    protected DiscountValue discountValue2;
    protected final Object discountValue1Lock;
    protected final Object discountValue2Lock;


    public CompositeNumericalDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        this.discountValue1 = discountValue1;
        this.discountValue2 = discountValue2;
        this.discountValue1Lock = new Object();
        this.discountValue2Lock = new Object();
    }

    public abstract int calcDiscount(List<ProductDTO> basketProducts);
}
