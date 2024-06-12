package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class CondDiscount extends Discount{
    private Rule<UserDTO, List<ProductDTO>> discountRule;
    CondDiscount(List<DiscountValue> discountValue, List<String> discountValueOperators, List<Rule<UserDTO, List<ProductDTO>>> discountRule, List<String> discountRuleOperators) {
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
                    case "COND" -> rule = new CondRule<>(rule, rules.get(i + 1));
                }
            }
        }
        this.discountRule = rule;
    }

    public String getDiscountRulesDescriptions() {
        return this.discountRule.getDescription();
    }
}
