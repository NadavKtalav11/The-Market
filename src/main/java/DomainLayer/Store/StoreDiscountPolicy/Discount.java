package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class Discount {
    protected DiscountValue discountValue;

    public Discount(List<DiscountValue> discountValue, List<String> operators) {
        setDiscountValue(discountValue, operators);
    }

    public void setDiscountValue(List<DiscountValue> discountValues, List<String> operators) {
        DiscountValue discountValue = discountValues.get(0);
        if (discountValues.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "MAX" -> discountValue = new MaxDiscount(discountValue, discountValues.get(i + 1));
                    case "ADDITION" -> discountValue = new AdditionDiscount(discountValue, discountValues.get(i + 1));
                }
            }
        }
        this.discountValue = discountValue;
    }

    public int calcDiscount(List<ProductDTO> basketProducts, UserDTO userDTO) {
        return discountValue.calcDiscount(basketProducts);
    }

    public DiscountValue getDiscountValue() {
        return discountValue;
    }
}
