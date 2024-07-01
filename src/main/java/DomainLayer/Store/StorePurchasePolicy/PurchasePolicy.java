package DomainLayer.Store.StorePurchasePolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static Util.ExceptionsEnum.*;

public class PurchasePolicy {
    private List<String> usersIdsActivatePolicy;
    private List<String> productNamesActivatePolicy;
    private Purchase purchase;

    private final Object usersLock;
    private final Object productLock;
    private List<Rule> purchaseRules;

    public PurchasePolicy()
    {
        this.usersIdsActivatePolicy = new ArrayList<>();
        this.productNamesActivatePolicy = new ArrayList<>();
        this.purchase = new Purchase(); //Default purchase policy
        usersLock = new Object();
        productLock = new Object();
        this.purchaseRules = new ArrayList<>();
    }

    public boolean checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        for (Rule rule : purchaseRules)
        {
            if (!rule.checkRule(userDTO, products))
                return false;
        }
        return true;
    }

    public void addRule(List<Rule> rules, List<String> operators)
    {
        Rule rule = rules.get(0);
        if(rules.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "AND" -> rule = new AndRule(rule, rules.get(i + 1));
                    case "OR" -> rule = new OrRule(rule, rules.get(i + 1));
                    case "ONLY IF" -> rule = new CondRule(rule, rules.get(i + 1));
                }
            }
        }
        purchaseRules.add(rule);
    }

    public List<String> getRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Rule rule : purchaseRules) {
            rulesDescriptions.add(rule.getDescription());
        }
        return rulesDescriptions;
    }

    public void composeCurrentStoreRules(int ruleNum1, int ruleNum2, String Operator)
    {
        if (ruleNum1 < purchaseRules.size() && ruleNum2 < purchaseRules.size())
        {
            Rule rule1 = purchaseRules.get(ruleNum1);
            Rule rule2 = purchaseRules.get(ruleNum2);

            // Create a list of indices and sort in reverse order
            List<Integer> indices = Arrays.asList(ruleNum1, ruleNum2);
            indices.sort(Collections.reverseOrder());

            // Remove rules in reverse order of indices
            for (int index : indices) {
                removeRule(index);
            }

            addRule(List.of(rule1, rule2), List.of(Operator));
        }
        else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }

    public void removeRule(int ruleNum) {
        if (ruleNum < purchaseRules.size())
            purchaseRules.remove(ruleNum);
        else throw new IllegalArgumentException(InvalidRuleIndex.toString());
    }
}
