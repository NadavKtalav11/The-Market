package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class AndRule extends CompositeRule {

    public AndRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return getRule1().checkRule(user, products) && getRule2().checkRule(user, products);
    }

    @Override
    public String getDescription() {
        return " (" + getRule1().getDescription() + " and " + getRule2().getDescription() + ") ";
    }
}
