package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.List;


public class OrRule<T, U> extends CompositeRule<T, U> {
    public OrRule(List<Rule<T,U>> rules) {
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
