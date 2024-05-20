package DomainLayer.Store;

import java.util.LinkedList;
import java.util.List;

public class Store {
    private int store_ID;
    private List<Product> storeProducts = new LinkedList<Product>();
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
        storeProducts.add(new Product(productName, price, quantity));
    }

    public void closeStore()
    {
        this.isOpened = false;
    }
}
