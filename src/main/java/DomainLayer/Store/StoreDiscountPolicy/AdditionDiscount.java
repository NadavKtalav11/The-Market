package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;

import java.util.List;


public class AdditionDiscount extends CompositeNumericalDiscount {
    public AdditionDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2);
    }

    @Override
    public int calcDiscount() {
        //todo: implement this
        return -1;
    }
}
