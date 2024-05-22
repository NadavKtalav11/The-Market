package DomainLayer.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket {
    private final int storeId;
    Map<String, List<Integer>> products = new HashMap<>();
    int basketPrice;

    public Basket(int storeId) {
        this.storeId = storeId;
        this.basketPrice = 0;
    }

    public int getStoreId()
    {
        return this.storeId;
    }

    public int getBasketPrice()
    {
        return this.basketPrice;
    }

    public void setBasketPrice(int price)
    {
        this.basketPrice = price;
    }


    public void addProduct(String productName, int quantity, int totalPrice)
    {
        List<Integer> quantityAndPrice = new ArrayList<>();
        quantityAndPrice.add(quantity);
        quantityAndPrice.add(totalPrice);
        products.put(productName, quantityAndPrice);
    }

    public void modifyProduct(String productName, int quantity, int totalPrice)
    {
        if (!products.containsKey(productName))
        {
            throw new IllegalArgumentException("The item you try to edit is not in your basket. Please add the item before attempting to modify it.");
        }

        List<Integer> quantityAndPrice = products.get(productName);
        quantityAndPrice.set(0, quantity);
        quantityAndPrice.set(1, totalPrice);
    }

    public void calcBasketPrice()
    {
        int totalPrice = 0;
        for (String productName : products.keySet()) {
            int totalItemPrice = products.get(productName).get(1);
            totalPrice += totalItemPrice;
        }

        setBasketPrice(totalPrice);
    }

    public boolean checkIfProductInBasket(String productName)
    {
        return products.containsKey(productName);
    }

    public void removeItemFromBasket(String productName)
    {
        products.remove(productName);
    }

}
