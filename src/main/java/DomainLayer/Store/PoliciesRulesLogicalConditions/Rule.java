package DomainLayer.Store.PoliciesRulesLogicalConditions;

public interface Rule<T, U> {
    public boolean checkRule(T user, U products);
}
