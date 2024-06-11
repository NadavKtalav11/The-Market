package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.List;

public abstract class CompositeRule<T, U> implements Rule<T, U> {
    protected Rule<T,U> rule1;
    protected Rule<T,U> rule2;

    public CompositeRule(Rule<T,U> rule1, Rule<T,U> rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    public Rule<T, U> getRule1() {
        return rule1;
    }

    public Rule<T, U> getRule2() {
        return rule2;
    }

    public abstract String getDescription();

    public abstract boolean checkRule(T user, U products);
}
