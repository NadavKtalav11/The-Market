package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.DiscountValueDTO;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.List;

import static Util.ExceptionsEnum.InvalidRuleIndex;

public class DiscountPolicy {
    private List<Discount> discountRules;

    private final Object userIdLock;
    private final Object productNameLock;

    public DiscountPolicy()
    {
        this.discountRules = new ArrayList<>();
        userIdLock = new Object();
        productNameLock = new Object();
    }

    public int calcDiscountPolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        int totalDiscount = 0;
        for (Discount discountRule : discountRules)
        {
            totalDiscount += discountRule.calcDiscount(products, userDTO);
        }
        return totalDiscount;
    }

    public void addCondRule(List<Rule<UserDTO, List<ProductDTO>>> rules, List<String> logicalOperators, List<DiscountValue> discDetails, List<String> numericalOperators) {
        discountRules.add(new CondDiscount(discDetails, numericalOperators, rules, logicalOperators));
    }

    public void addSimple(List<DiscountValue> discDetails, List<String> discountValueOperators) {
        discountRules.add(new Discount(discDetails, discountValueOperators));
    }

    public void addRule(Discount discount) {
        discountRules.add(discount);
    }

    public void removeRule(int ruleNum) {
        if (ruleNum < discountRules.size())
            discountRules.remove(ruleNum);
        else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public List<Discount> getDiscountRules() {
        return discountRules;
    }
}
