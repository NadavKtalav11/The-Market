package DomainLayer.Store;

import java.util.ArrayList;
import java.util.List;

public class PurchasePolicy {
    private List<String> usersIdsActivatePolicy;
    private List<String> productNamesActivatePolicy;
    private Purchase purchase;

    private final Object usersLock;
    private final Object productLock;

    public PurchasePolicy()
    {
        this.usersIdsActivatePolicy = new ArrayList<>();
        this.productNamesActivatePolicy = new ArrayList<>();
        this.purchase = new Purchase(); //Default purchase policy
        usersLock = new Object();
        productLock = new Object();
    }

    public boolean checkPurchasePolicy(String userId, String productName)
    {
        //No requirement to policy policy, return true
        return true;
    }

}
