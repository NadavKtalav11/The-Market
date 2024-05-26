package DomainLayer.Store;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Store {
    private int store_ID;
    private Map<String, Product> storeProducts = new HashMap<String, Product>();
    private boolean isOpened;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private double rating;
    private int numOfRatings;
    private Map<Integer, Integer> receiptsIdsUserIds; //<receiptId, userId>


    private Object storeProductLock;
    private Object storeIdLock;
    private Object isOpenedLock;

    Store(int store_ID)
    {
        this.store_ID = store_ID;
        this.isOpened = true;
        storeIdLock= new Object();
        discountPolicy= new DiscountPolicy();
        purchasePolicy = new PurchasePolicy();
        storeProductLock= new Object();
        isOpenedLock = new Object();
        this.receiptsIdsUserIds = new HashMap<>();
    }


    public void returnProductToStore(Map<String, Integer> products){
        for (String product : products.keySet()){
            storeProducts.get(product).addToStock(products.get(product));
        }
    }

    public int getStoreID()
    {
        synchronized (storeIdLock) {
            return store_ID;
        }
    }

    public void addProduct(String productName, int price, int quantity, String description, String categoryStr) {
        Category category = Category.fromString(categoryStr);
        synchronized (storeProductLock) {
            storeProducts.put(productName, new Product(productName, price, quantity, description, category));
        }
    }

    public Product getProductByName(String productName)
    {
        synchronized (storeProductLock) {
            return storeProducts.get(productName);
        }
    }

    public boolean checkProductExists(String productName)
    {
        synchronized (storeProductLock) {
            return storeProducts.containsKey(productName);
        }
    }

    public boolean checkProductQuantity(String productName, int quantity)
    {
        Product productToCheck = getProductByName(productName);
        return productToCheck.getQuantity() >= quantity; //true if the quantity in the store is bigger than the quantity a user want to add
    }

    public int calcPriceInStore(String productName, int quantity, int userId)
    {
        Product productToCalc = getProductByName(productName);
        return productToCalc.getPrice() * quantity;

        //in the future, add check for discount using the discount policy
    }

    public void removeProduct(String productName){
        synchronized (storeProductLock) {
            storeProducts.remove(productName);
        }
    }

    public void updateProduct(String productName, int price, int quantity, String description, String categoryStr){
        synchronized (storeProductLock) {
            storeProducts.get(productName).setPrice(price);
            storeProducts.get(productName).setQuantity(quantity);
            storeProducts.get(productName).setDescription(description);
            Category category = Category.fromString(categoryStr);
            storeProducts.get(productName).setCategory(category);
        }
    }

    public void closeStore()
    {
        synchronized (isOpenedLock) {
            this.isOpened = false;
        }
    }

    public boolean getIsOpened()
    {
        synchronized (isOpenedLock) {
            return this.isOpened;
        }
    }

    public List<String> getProducts()
    {
        List<String> productsList;

        synchronized (storeProductLock) {
            Set<String> productsSet = storeProducts.keySet();
            productsList = new ArrayList<>(productsSet);
            return productsList;
        }
    }

    public boolean checkDiscountPolicy(int userId, String productName)
    {
        return this.discountPolicy.checkDiscountPolicy(userId, productName);
    }

    public boolean checkPurchasePolicy(int userId, String productName)
    {
        return this.purchasePolicy.checkPurchasePolicy(userId, productName);
    }

    public List<String> matchProducts(String productName, String categoryStr, List<String> keywords)
    {
        synchronized (storeProductLock) {
            List<Product> products = storeProducts.values().stream().toList();
            return products.stream()
                    .filter(product -> productName == null || product.getProductName().toLowerCase().contains(productName.toLowerCase()))
                    .filter(product -> categoryStr == null || product.getCategoryName().equals(categoryStr.toUpperCase()))
                    .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .map(Product::getProductName)
                    .collect(Collectors.toList());
        }
    }

    public List<String> filterProducts(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, List<String> productsFromSearch, Double storeMinRating)
    {
        List<Product> products = new ArrayList<>();

            for (String productName : productsFromSearch) {
                synchronized (storeProductLock) {
                    products.add(storeProducts.get(productName));
                }
            }
            return products.stream()
                    .filter(product -> categoryStr == null || product.getCategoryName().equals(categoryStr.toUpperCase()))
                    .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                    .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                    .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                    .filter(product -> minRating == null || product.getRating() >= minRating)
                    .filter(product -> storeMinRating == null || this.rating >= storeMinRating)
                    .map(Product::getProductName)
                    .collect(Collectors.toList());

    }

    public void addReceipt(int receiptId, int userId)
    {
        receiptsIdsUserIds.put(receiptId, userId);
    }
}
