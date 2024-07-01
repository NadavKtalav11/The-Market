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

    public void addCondRule(List<Rule> rules, List<String> logicalOperators, List<DiscountValue> discDetails, List<String> numericalOperators) {
        discountRules.add(new CondDiscount(discDetails, numericalOperators, rules, logicalOperators));
    }

    public void composeCurrentCondDiscountRules(int ruleNum1, int ruleNum2, List<String> logicalOperators, List<String> numericalOperators) {
        if (ruleNum1 < discountRules.size() && ruleNum2 < discountRules.size()) {
            List<Rule> rules = new ArrayList<>();
            List<DiscountValue> discDetails = new ArrayList<>();
            if(discountRules.get(ruleNum1) instanceof CondDiscount discountRule1 && discountRules.get(ruleNum2) instanceof CondDiscount discountRule2) {
                rules.add(discountRule1.getDiscountRule());
                rules.add(discountRule2.getDiscountRule());
                discDetails.add(discountRule1.getDiscountValue());
                discDetails.add(discountRule2.getDiscountValue());
                removeRule(ruleNum1);
                removeRule(ruleNum2);
                addCondRule(rules, logicalOperators, discDetails, numericalOperators);
            } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
        } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public void composeCurrentSimpleDiscountRules(int ruleNum1, int ruleNum2, List<String> discountValueOperators) {
        if (ruleNum1 < discountRules.size() && ruleNum2 < discountRules.size()) {
            List<DiscountValue> discDetails = new ArrayList<>();
            if(!(discountRules.get(ruleNum1) instanceof CondDiscount) && !(discountRules.get(ruleNum2) instanceof CondDiscount))
            {
                discDetails.add(discountRules.get(ruleNum1).getDiscountValue());
                discDetails.add(discountRules.get(ruleNum2).getDiscountValue());
                removeRule(ruleNum1);
                removeRule(ruleNum2);
                addSimple(discDetails, discountValueOperators);
            } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
        } else throw new IllegalArgumentException(InvalidRuleIndex.toString());
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

    public List<String> getRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : discountRules) {
            rulesDescriptions.add(discount.getDescription());
        }
        return rulesDescriptions;
    }

    public List<String> getCondDiscountRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : discountRules) {
            if(discount instanceof CondDiscount condDiscount) {
                rulesDescriptions.add(condDiscount.getDescription());
            }
        }
        return rulesDescriptions;
    }

    public List<String> getSimpleDiscountRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Discount discount : discountRules) {
            if(!(discount instanceof CondDiscount)) {
                rulesDescriptions.add(discount.getDescription());
            }
        }
        return rulesDescriptions;
    }
}
