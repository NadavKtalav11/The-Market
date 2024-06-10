package DomainLayer.Store;

import java.util.List;

public class PurchaseAndRule<T, U> extends PurchaseCompositeRule<T, U>{

    public PurchaseAndRule(List<Rule<T,U>> rules) {
        super(rules);
    }

    @Override
    public boolean checkRule(T user, U products) {
        for (Rule<T, U> rule : rules) {
            if (!rule.checkRule(user, products)) {
                return false;
            }
        }
        return true;
    }
}
