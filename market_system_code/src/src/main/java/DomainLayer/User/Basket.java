package DomainLayer.User;

import java.util.HashMap;
import java.util.Map;

public class Basket {
    private final int storeId;
    Map<Integer, Integer> products = new HashMap<>();

    public Basket(int storeId) {
        this.storeId = storeId;
    }

    public int getId()
    {
        return this.storeId;
    }

    public void addProduct(int productId, int quantity)
    {
        products.put(productId, quantity);
    }
}
