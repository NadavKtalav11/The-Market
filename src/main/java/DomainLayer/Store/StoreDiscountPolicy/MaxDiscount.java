package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;

import java.util.List;


public class MaxDiscount extends CompositeNumericalDiscount {

    public MaxDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2);
    }

    @Override
    public int calcDiscount() {
        //todo: implement this
        return -1;
    }
}
