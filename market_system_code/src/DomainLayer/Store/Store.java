package DomainLayer.Store;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Store {
    private int store_ID;
    private Map<String, Product> storeProducts = new HashMap<String, Product>();
    private boolean isOpened;

    Store(int store_ID)
    {
        this.store_ID = store_ID;
        this.isOpened = true;
    }

    public int getStoreID()
    {
        return store_ID;
    }

    public void addProduct(String productName, int price, int quantity){
        storeProducts.put(productName, new Product(productName, price, quantity));
    }

    public void removeProduct(String productName){
        storeProducts.remove(productName);
    }

    public void updateProduct(String productName, int price, int quantity){
        storeProducts.get(productName).setPrice(price);
        storeProducts.get(productName).setQuantity(quantity);
    }

    public void closeStore()
    {
        this.isOpened = false;
    }
}
