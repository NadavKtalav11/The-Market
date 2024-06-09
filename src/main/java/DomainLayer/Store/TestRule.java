package DomainLayer.Store;
import com.vaadin.flow.component.html.Pre;

import java.util.function.Predicate;
import java.util.function.BiPredicate;


public interface TestRule<T,U> {
    <T, U> BiPredicate<T, U> getPredicate();
}
