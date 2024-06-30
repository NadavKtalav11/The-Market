package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class XorRule extends CompositeRule  {

    public XorRule(Rule rule1, Rule rule2) {
        super(rule1, rule2);
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return rule1.checkRule(user, products) ^ rule2.checkRule(user, products);
    }

    @Override
    public String getDescription() {
        return " (" + rule1.getDescription() + " xor " + rule2.getDescription() + ") ";
    }

}
