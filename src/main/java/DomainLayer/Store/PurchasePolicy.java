package DomainLayer.Store;

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
    private List<Rule<UserDTO, Map<String, List<Integer>>>> purchaseRules;

    public PurchasePolicy()
    {
        this.usersIdsActivatePolicy = new ArrayList<>();
        this.productNamesActivatePolicy = new ArrayList<>();
        this.purchase = new Purchase(); //Default purchase policy
        usersLock = new Object();
        productLock = new Object();
    }

    public boolean checkPurchasePolicy(UserDTO userDTO, Map<String, List<Integer>> products)
    {
        for (Rule<UserDTO, Map<String, List<Integer>>> rule : purchaseRules)
        {
            if (!rule.checkRule(userDTO, products))
                return false;
        }
        return true;
    }

}
