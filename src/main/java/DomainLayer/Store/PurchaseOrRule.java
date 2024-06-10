package DomainLayer.Store;
import java.util.List;


public class PurchaseOrRule<T, U> extends PurchaseCompositeRule<T, U>{
    public PurchaseOrRule(Rule<T, U> rule1, Rule<T, U> rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(T user, U products) {
        return rule1.checkRule(user, products) || rule2.checkRule(user, products);
    }
}
