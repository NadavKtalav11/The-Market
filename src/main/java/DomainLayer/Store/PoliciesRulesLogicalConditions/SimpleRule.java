package DomainLayer.Store.PoliciesRulesLogicalConditions;

public class SimpleRule<T, U> implements Rule<T, U> {

    private TestRule rule;

    public SimpleRule(TestRule rule) {
        this.rule = rule;
    }

    public String getDescription() {
        return rule.getDescription();
    }

    @Override
    public boolean checkRule(T user, U products) {
        return rule.getPredicate().test(user, products);
    }

}
