package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.List;

public class AndRule<T, U> extends CompositeRule<T, U> {

    public AndRule(List<Rule<T,U>> rules) {
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
