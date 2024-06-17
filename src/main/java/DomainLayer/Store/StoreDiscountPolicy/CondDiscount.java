package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.*;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class CondDiscount extends Discount{
    private Rule<UserDTO, List<ProductDTO>> discountRule;
    public CondDiscount(List<DiscountValue> discountValue, List<String> discountValueOperators, List<Rule<UserDTO, List<ProductDTO>>> discountRule, List<String> discountRuleOperators) {
        super(discountValue, discountValueOperators);
        this.setDiscountRule(discountRule, discountRuleOperators);
    }

    public void setDiscountRule(List<Rule<UserDTO, List<ProductDTO>>> rules, List<String> operators)
    {
        Rule<UserDTO, List<ProductDTO>> rule = rules.get(0);
        if(rules.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "AND" -> rule = new AndRule<>(rule, rules.get(i + 1));
                    case "OR" -> rule = new OrRule<>(rule, rules.get(i + 1));
                    case "XOR" -> rule = new XorRule<>(rule, rules.get(i + 1));
                }
            }
        }
        this.discountRule = rule;
    }

    public String getDiscountRulesDescriptions() {
        return this.discountRule.getDescription();
    }

    @Override
    public int calcDiscount(List<ProductDTO> basketProducts , UserDTO userDTO) {
        if(discountRule.checkRule(userDTO, basketProducts))
            return super.calcDiscount(basketProducts, userDTO);
        return 0;
    }

    @Override
    public String getDescription() {
        return " (" + super.getDescription() + " only if " + discountRule.getDescription() + ") ";
    }
}
