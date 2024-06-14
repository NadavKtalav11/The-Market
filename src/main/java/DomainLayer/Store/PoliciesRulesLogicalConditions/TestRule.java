package DomainLayer.Store.PoliciesRulesLogicalConditions;

import java.util.function.BiPredicate;


public interface TestRule<T,U> {
    <T, U> BiPredicate<T, U> getPredicate();
    String getDescription();
}
