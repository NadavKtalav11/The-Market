package DomainLayer.Store;

import java.util.List;

public class PruchaseCondRule<T, U> implements Rule<T, U> {

    public List<Rule<T, U>> rules;
    public Rule<T, U> predicate;

    public PruchaseCondRule(Rule<T, U> predicate) {
        super();
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
