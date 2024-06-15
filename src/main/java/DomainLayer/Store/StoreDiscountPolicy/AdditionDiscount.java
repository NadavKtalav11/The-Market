package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.StoreDiscountPolicy.CompositeNumericalDiscount;
import Util.ProductDTO;

import java.util.List;


public class AdditionDiscount extends CompositeNumericalDiscount {
    public AdditionDiscount(DiscountValue discountValue1, DiscountValue discountValue2) {
        super(discountValue1, discountValue2);
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts) {
        int discount1 = discountValue1.calcDiscount(basketProducts);
        int discount2 = discountValue2.calcDiscount(basketProducts);

        return discount1 + discount2;
    }

}
