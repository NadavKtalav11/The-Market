package DomainLayer.Store.StorePurchasePolicy;

import DomainLayer.Store.PoliciesRulesLogicalConditions.CondRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.AndRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.OrRule;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class PurchasePolicy {
    private List<String> usersIdsActivatePolicy;
    private List<String> productNamesActivatePolicy;
    private Purchase purchase;

    private final Object usersLock;
    private final Object productLock;
    private List<Rule<UserDTO, List<ProductDTO>>> purchaseRules;

    public PurchasePolicy()
    {
        this.usersIdsActivatePolicy = new ArrayList<>();
        this.productNamesActivatePolicy = new ArrayList<>();
        this.purchase = new Purchase(); //Default purchase policy
        usersLock = new Object();
        productLock = new Object();
    }

    public boolean checkPurchasePolicy(UserDTO userDTO, List<ProductDTO> products)
    {
        for (Rule<UserDTO, List<ProductDTO>> rule : purchaseRules)
        {
            if (!rule.checkRule(userDTO, products))
                return false;
        }
        return true;
    }

    public void addRule(List<Rule<UserDTO, List<ProductDTO>>> rules, String operator, Rule<UserDTO, List<ProductDTO>> predicat)
    {
        if(rules.size() == 1)
            purchaseRules.add(rules.get(0));
        else
        {
            switch (operator) {
                case "AND" -> purchaseRules.add(new AndRule<>(rules));
                case "OR" -> purchaseRules.add(new OrRule<>(rules));
                case "COND" -> purchaseRules.add(new CondRule<>(rules, predicat));
            }
        }
    }

}
