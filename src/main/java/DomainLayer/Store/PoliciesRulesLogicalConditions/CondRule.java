package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class CondRule extends CompositeRule {

    public CondRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        if (getRule1().checkRule(user, products)) {
            return getRule2().checkRule(user, products);
        }
        return true;
    }

    @Override
    public String getDescription() {
        return " (" + getRule1().getDescription() + " only if " + getRule2().getDescription() + ") ";
    }
}
