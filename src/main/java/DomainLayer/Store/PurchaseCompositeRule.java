package DomainLayer.Store;

import java.util.List;

public abstract class PurchaseCompositeRule<T, U> implements Rule<T, U>{
    private List<Rule> rules;

    public PurchaseCompositeRule(List<Rule> rules) {
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void removeRule(Rule rule) {
        rules.remove(rule);
    }

    public abstract boolean checkRule(T user, U products);
}
