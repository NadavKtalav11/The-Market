package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;
import Util.ProductDTO;

import java.util.List;


public class MaxDiscount extends CompositeNumericalDiscount {

    public MaxDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2);
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts) {
        //check which discount is bigger and return it
        int discount1 = getDiscountValue1().calcDiscount(basketProducts);
        int discount2 = getDiscountValue2().calcDiscount(basketProducts);
        return Math.max(discount1, discount2);
    }

    @Override
    public String getDescription() {
        return " (Max between " + getDiscountValue1().getDescription() + " and " + getDiscountValue2().getDescription() + ") ";
    }
}
