package DomainLayer.User;

import java.util.*;

public class Cart {
    Map<String, Basket> baskets ; //key = storeID
    private int cartPrice;

    private final Object basketsLock;
    private final Object priceLock;

    public Cart() {
        this.cartPrice = 0;
        basketsLock = new Object();
        baskets = new HashMap<>();
        priceLock = new Object();
    }

    public int getCartPrice()
    {
        calcCartTotal();
        return cartPrice;
    }

    private void setCartPrice(int price)
    {
        synchronized (priceLock) {
            this.cartPrice = price;
        }
    }

    public void addItemsToCart(String productName, int quantity, String storeId, int totalPrice)
    {
        Basket basket;
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                basket = baskets.get(storeId);
            } else {
                basket = new Basket(storeId);
                baskets.put(storeId, basket);
            }
            basket.addProduct(productName, quantity, totalPrice);
        }

    }

    public void modifyProductInCart(String productName, int quantity, String storeId, int totalPrice)
    {
        synchronized (basketsLock) {
            if (!baskets.containsKey(storeId)) {
                throw new IllegalArgumentException("You can only modify items in your cart from existing store's basket.");
            }
            else
            {
                baskets.get(storeId).modifyProduct(productName, quantity, totalPrice);
            }
        }

    }

    public void calcCartTotal()
    {
        int totalCartPrice = 0;
        synchronized (basketsLock) {
            for (String storeId : baskets.keySet()) {
                Basket basket = baskets.get(storeId);
                totalCartPrice = totalCartPrice + basket.getBasketPrice();
            }
        }
        setCartPrice(totalCartPrice);

    }

    public boolean checkIfProductInCart(String productName, String storeId)
    {
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                return baskets.get(storeId).checkIfProductInBasket(productName);
            }
        }

        throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
    }

    public void removeItemFromCart(String productName, String storeId)
    {
        synchronized (basketsLock) {
            if (baskets.containsKey(storeId)) {
                baskets.get(storeId).removeItemFromBasket(productName);
            }
            else{
                throw new IllegalArgumentException("The store id" + storeId + "you entered is invalid");
            }
        }
    }

    public boolean isCartEmpty()
    {
        synchronized (basketsLock) {
            for (String storeId : baskets.keySet()) {
                Basket basket = baskets.get(storeId);
                if (!basket.isBasketEmpty())
                    return false;
            }
        }
        return true;

    }

    public Map<String, List<Integer>> getProductsDetailsByStore(String store_ID)
    {
        synchronized (basketsLock) {
            return baskets.get(store_ID).getProducts();
        }
    }

    public Map<String, Integer> getProductsQuantityByStore(String store_ID)
    {
        Map<String, List<Integer>> products = this.getProductsDetailsByStore(store_ID);
        //create new map, contains only the quantity of each product
        Map<String, Integer> productsQuantity = new HashMap<>();

        for (Map.Entry<String, List<Integer>> entry : products.entrySet()) {
            String productName = entry.getKey();
            List<Integer> details = entry.getValue();

            if (details != null && !details.isEmpty()) {
                Integer quantity = details.get(0);
                productsQuantity.put(productName, quantity);
            }
        }

        return productsQuantity;
    }


    public List<String> getCartStores()
    {
        /*this function returns the stores from which the user added his products*/
        synchronized (basketsLock) {
            return new ArrayList<>(baskets.keySet());
        }
    }
}
