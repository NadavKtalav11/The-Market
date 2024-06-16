package DomainLayer.Store.PoliciesRulesLogicalConditions;

public class XorRule<T, U> extends CompositeRule<T, U>  {

    public XorRule(Rule<T, U> rule1, Rule<T, U> rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(T user, U products) {
        return rule1.checkRule(user, products) ^ rule2.checkRule(user, products);
    }

    @Override
    public String getDescription() {
        return " (" + rule1.getDescription() + " xor " + rule2.getDescription() + ") ";
    }

}
