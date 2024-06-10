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
                case "AND" -> purchaseRules.add(new PurchaseAndRule<>(rules));
                case "OR" -> purchaseRules.add(new PurchaseOrRule<>(rules));
                case "COND" -> purchaseRules.add(new PruchaseCondRule<>(rules, predicat));
            }
        }
    }

}
