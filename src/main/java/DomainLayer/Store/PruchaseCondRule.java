package DomainLayer.Store;

import java.util.List;

public class PruchaseCondRule<T, U> extends PurchaseCompositeRule<T, U> {

    public Rule<T, U> predicate;

    public PruchaseCondRule(List<Rule<T, U>> rules, Rule<T, U> predicate) {
        super(rules);
        this.predicate = predicate;
    }

    //implement it such that if the predicate is true, then the all the rules are true
    @Override
    public boolean checkRule(T user, U products) {
        if (predicate.checkRule(user, products))
        {
            for (Rule<T, U> rule : rules) {
                if (!rule.checkRule(user, products)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
