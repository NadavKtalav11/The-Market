package DomainLayer.User;

import java.util.LinkedList;
import java.util.List;

public class Cart {
    private List<Basket> baskets;

    public Cart() {
        this.baskets = new LinkedList<>();
    }

    public void addItemsToCart(int productId, int quantity, int storeId)
    {
        for (Basket basket : baskets) {
            if (basket.getId() == storeId) {
                basket.addProduct(productId, quantity);
                break;
            }
        }
        throw new IllegalArgumentException("Product with ID " + productId + " not found");
    }
}
