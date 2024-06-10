package DomainLayer.Store;

import Util.ProductDTO;
import Util.UserDTO;
import org.w3c.dom.ls.LSException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
        this.purchaseRules = new ArrayList<>();
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

    public void addRule(List<Rule<UserDTO, List<ProductDTO>>> rules, List<String> operators)
    {
        Rule<UserDTO, List<ProductDTO>> rule = rules.get(0);
        if(rules.size() > 1) {
            for (int i = 0; i < operators.size(); i++) {
                switch (operators.get(i)) {
                    case "AND" -> rule = new PurchaseAndRule<>(rule, rules.get(i + 1));
                    case "OR" -> rule = new PurchaseOrRule<>(rule, rules.get(i + 1));
                    case "COND" -> rule = new PruchaseCondRule<>(rule, rules.get(i + 1));
                }
            }
        }
        purchaseRules.add(rule);
    }

    public List<String> getRulesDescriptions() {
        List<String> rulesDescriptions = new ArrayList<>();
        for (Rule<UserDTO, List<ProductDTO>> rule : purchaseRules) {
            rulesDescriptions.add(rule.getDescription());
        }
        return rulesDescriptions;
    }

    public void removeRule(int ruleNum) {
        purchaseRules.remove(ruleNum);
    }
}
