package DomainLayer.User;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Cart {
    Map<Integer, Basket> baskets = new HashMap<>();
    private int cartPrice;

    public Cart() {
        this.cartPrice = 0;
    }

    public int getCartPrice()
    {
        return this.cartPrice;
    }

    public void setCartPrice(int price)
    {
        this.cartPrice = price;
    }

    public void addItemsToCart(String productName, int quantity, int storeId, int totalPrice)
    {
        Basket basket;
        if (baskets.containsKey(storeId))
        {
            basket = baskets.get(storeId);
        }
        else
        {
            basket = new Basket(storeId);
            baskets.put(storeId, basket);
        }
        basket.addProduct(productName, quantity, totalPrice);
    }

    public void modifyProductInCart(String productName, int quantity, int storeId, int totalPrice)
    {
        if (!baskets.containsKey(storeId))
        {
            throw new IllegalArgumentException("You can only modify items in your cart from existing store's basket.");
        }

    }

    public void calcCartTotal()
    {
        int totalCartPrice = 0;
        for (Integer storeId : baskets.keySet()) {
            Basket basket = baskets.get(storeId);
            basket.calcBasketPrice();
            totalCartPrice += basket.getBasketPrice();
        }

        setCartPrice(totalCartPrice);
    }

    public boolean checkIfProductInCart(String productName, int storeId)
    {
        if (baskets.containsKey(storeId))
        {
            return baskets.get(storeId).checkIfProductInBasket(productName);
        }

        throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
    }

    public void removeItemFromCart(String productName, int storeId)
    {
        if (baskets.containsKey(storeId))
        {
            baskets.get(storeId).removeItemFromBasket(productName);
        }

        throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
    }


}
