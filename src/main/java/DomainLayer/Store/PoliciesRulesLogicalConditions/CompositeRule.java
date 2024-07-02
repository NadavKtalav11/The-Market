package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public abstract class CompositeRule implements Rule {
    protected Rule rule1;
    protected Rule rule2;
    protected final Object rule1Lock;
    protected final Object rule2Lock;

    public CompositeRule(Rule rule1, Rule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
        this.rule1Lock = new Object();
        this.rule2Lock = new Object();
    }

    public Rule getRule1() {
        synchronized (rule1Lock) {
            return rule1;
        }
    }

    public Rule getRule2() {
        synchronized (rule2Lock) {
            return rule2;
        }
    }

    public abstract String getDescription();

    public abstract boolean checkRule(UserDTO user, List<ProductDTO> products);
}
