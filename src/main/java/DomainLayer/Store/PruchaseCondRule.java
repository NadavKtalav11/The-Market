package DomainLayer.Store;

import java.util.List;

public class PruchaseCondRule<T, U> extends PurchaseCompositeRule<T, U> {


    public PruchaseCondRule(Rule<T,U> rule1, Rule<T,U> rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(T user, U products) {
        if (rule1.checkRule(user, products)) {
            return rule2.checkRule(user, products);
        }
        return false;
    }
}
