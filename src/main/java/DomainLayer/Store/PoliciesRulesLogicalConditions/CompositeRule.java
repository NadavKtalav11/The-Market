package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public abstract class CompositeRule implements Rule {
    protected Rule rule1;
    protected Rule rule2;

    public CompositeRule(Rule rule1, Rule rule2) {
        this.rule1 = rule1;
        this.rule2 = rule2;
    }

    public Rule getRule1() {
        return rule1;
    }

    public Rule getRule2() {
        return rule2;
    }

    public abstract String getDescription();

    public abstract boolean checkRule(UserDTO user, List<ProductDTO> products);
}
