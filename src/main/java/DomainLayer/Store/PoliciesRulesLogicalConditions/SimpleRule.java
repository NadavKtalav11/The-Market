package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class SimpleRule implements Rule {

    private TestRule rule;

    public SimpleRule(TestRule rule) {
        this.rule = rule;
    }

    public String getDescription() {
        return rule.getDescription();
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        return rule.test(user, products);
    }

}
