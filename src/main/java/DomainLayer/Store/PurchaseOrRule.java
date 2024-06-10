package DomainLayer.Store;
import java.util.List;


public class PurchaseOrRule<T, U> extends PurchaseCompositeRule<T, U>{
    public PurchaseOrRule(List<Rule<T,U>> rules) {
        super(rules);
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
}
