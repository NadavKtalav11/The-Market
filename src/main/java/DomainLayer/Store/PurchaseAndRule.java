package DomainLayer.Store;

import java.util.List;

public class PurchaseAndRule<T, U> implements Rule<T, U>{

    public List<Rule<T, U>> rules;
    public PurchaseAndRule() {
        super();
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

    public List<Rule<T, U>> getRules() {
        return rules;
    }
}
