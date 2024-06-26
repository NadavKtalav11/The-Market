package DomainLayer.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Basket {
    private final String storeId;
    private Map<String, List<Integer>> products; //key = product name, value = [quantity, products total price]
    int basketPrice;
    private final Object basketPriceLock;
    private final Object productsLock;


    public Basket(String storeId) {
        this.storeId = storeId;
        this.basketPrice = 0;
        basketPriceLock = new Object();
        products = new HashMap<>();
        productsLock =new Object();
    }

    public String getStoreId()
    {
        return this.storeId;
    }



    public int getBasketPrice()
    {
        synchronized (basketPriceLock) {
            calcBasketPrice();
            return this.basketPrice;
        }
    }

    public void setBasketPrice(int price)
    {
        synchronized (basketPriceLock) {
            this.basketPrice = price;
        }
    }

    public synchronized Map<String, List<Integer>> getProducts() {
        synchronized (productsLock) {
            return products;
        }
    }

    public synchronized void addProduct(String productName, int quantity, int totalPrice)
    {
        List<Integer> quantityAndPrice = new ArrayList<>();
        synchronized (productsLock) {
            quantityAndPrice.add(quantity);
            quantityAndPrice.add(totalPrice);
            products.put(productName, quantityAndPrice);
        }
    }

    public synchronized void modifyProduct(String productName, int quantity, int totalPrice)
    {
        synchronized (productsLock) {
            if (!products.containsKey(productName)) {
                throw new IllegalArgumentException("The item you try to edit is not in your basket. Please add the item before attempting to modify it.");
            }
        }
        List<Integer> quantityAndPrice;
        synchronized (productsLock) {
            quantityAndPrice = products.get(productName);
            quantityAndPrice.set(0, quantity);
            quantityAndPrice.set(1, totalPrice);
        }
    }

    public synchronized void calcBasketPrice()
    {
        int totalPrice = 0;
        synchronized (productsLock) {
            for (String productName : products.keySet()) {
                int totalItemPrice = products.get(productName).get(1);
                totalPrice += totalItemPrice;
            }
        }
        setBasketPrice(totalPrice);
    }

    public synchronized boolean checkIfProductInBasket(String productName)
    {
        synchronized (productsLock) {
            return products.containsKey(productName);
        }

    }

    public synchronized void removeItemFromBasket(String productName)
    {
        synchronized (productsLock) {
            products.remove(productName);
        }
    }

    public synchronized boolean isBasketEmpty()
    {
        synchronized (productsLock){
            return products.isEmpty();
        }

    }


}
