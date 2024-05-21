package DomainLayer.Store;

import java.util.*;
import java.util.ArrayList;

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

    public Product getProductByName(String productName)
    {
        return storeProducts.get(productName);
    }

    public boolean checkProductQuantity(String productName, int quantity)
    {
        if (storeProducts.containsKey(productName))
        {
            Product productToCheck = getProductByName(productName);
            return productToCheck.getQuantity() >= quantity; //true if the quantity in the store is bigger than the quantity a user want to add
        }
        return false;
    }

    public int calcPriceInStore(String productName, int quantity, int userId)
    {
        Product productToCalc = getProductByName(productName);
        return productToCalc.getPrice() * quantity;

        //in the future, add check for discount using the discount policy
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

    public boolean getIsOpened()
    {
        return this.isOpened;
    }

    public List<String> getProducts()
    {
        Set<String> productsSet = storeProducts.keySet();
        List<String> productsList = new ArrayList<>(productsSet);
        return productsList;
    }
}
