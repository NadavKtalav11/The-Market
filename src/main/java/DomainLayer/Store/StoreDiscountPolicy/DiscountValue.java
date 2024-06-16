package DomainLayer.Store.StoreDiscountPolicy;

import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public interface DiscountValue {
    public int calcDiscount(List<ProductDTO> basketProducts);

    public String getDescription();
}
