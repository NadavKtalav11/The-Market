package DomainLayer.Store.PoliciesRulesLogicalConditions;

import Util.ProductDTO;
import Util.UserDTO;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;

public interface Rule extends Serializable {
    public boolean checkRule(UserDTO user, List<ProductDTO> products);

    public String getDescription();
}
