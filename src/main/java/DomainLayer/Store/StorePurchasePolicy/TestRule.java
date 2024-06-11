package DomainLayer.Store.StorePurchasePolicy;

import java.util.function.BiPredicate;


public interface TestRule<T,U> {
    <T, U> BiPredicate<T, U> getPredicate();
    String getDescription();
}
