package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public interface Rule {
    public boolean checkRule(UserDTO user, List<ProductDTO> products);

    public String getDescription();
}
