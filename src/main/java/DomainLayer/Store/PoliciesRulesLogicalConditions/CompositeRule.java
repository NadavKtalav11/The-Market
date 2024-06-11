package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.List;

public abstract class CompositeRule<T, U> implements Rule<T, U> {
    protected List<Rule<T,U>> rules;

    public CompositeRule(List<Rule<T,U>> rules) {
        this.rules = rules;
    }

    public List<Rule<T,U>> getRules() {
        return rules;
    }

    public void setRules(List<Rule<T,U>> rules) {
        this.rules = rules;
    }

    public void addRule(Rule<T,U> rule) {
        rules.add(rule);
    }

    public void removeRule(Rule<T,U> rule) {
        rules.remove(rule);
    }

    public abstract boolean checkRule(T user, U products);
}
