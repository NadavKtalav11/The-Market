package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.List;

public class CondRule<T, U> extends CompositeRule<T, U> {


    public CondRule(Rule<T,U> rule1, Rule<T,U> rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(T user, U products) {
        if (rule1.checkRule(user, products)) {
            return rule2.checkRule(user, products);
        }
        return false;
    }

    @Override
    public String getDescription() {
        return rule2.getDescription() + " only if " + rule1.getDescription();
    }
}
