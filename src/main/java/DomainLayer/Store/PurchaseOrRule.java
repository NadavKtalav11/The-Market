package DomainLayer.Store;
import java.util.List;


public class PurchaseOrRule<T, U> implements Rule<T, U>{
    public List<Rule<T, U>> rules;
    public PurchaseOrRule() {
        super();
    }

    @Override
    public boolean checkRule(T user, U products) {
        for (Rule<T, U> rule : rules) {
            if (rule.checkRule(user, products)) {
                return true;
            }
        }
        return false;
    }

    public List<Rule<T, U>> getRules() {
        return rules;
    }

}
