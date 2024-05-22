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

    Store(int store_ID)
    {
        this.store_ID = store_ID;
        this.isOpened = true;
    }

    public int getStoreID()
    {
        return store_ID;
    }

    public void addProduct(String productName, int price, int quantity, String description, String categoryStr){
        Category category = Category.fromString(categoryStr);
        storeProducts.put(productName, new Product(productName, price, quantity, description, category));
    }

    public Product getProductByName(String productName)
    {
        return storeProducts.get(productName);
    }

    public boolean checkProductExists(String productName)
    {
        return storeProducts.containsKey(productName);
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
        storeProducts.remove(productName);
    }

    public void updateProduct(String productName, int price, int quantity, String description, String categoryStr){
        storeProducts.get(productName).setPrice(price);
        storeProducts.get(productName).setQuantity(quantity);
        storeProducts.get(productName).setDescription(description);
        Category category = Category.fromString(categoryStr);
        storeProducts.get(productName).setCategory(category);
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
        List<Product> products = storeProducts.values().stream().toList();
        return products.stream()
                .filter(product -> productName == null || product.getProductName().toLowerCase().contains(productName.toLowerCase()))
                .filter(product -> categoryStr == null || product.getCategoryName().equals(categoryStr))
                .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .map(Product::getProductName)
                .collect(Collectors.toList());
    }

    public List<String> filterProducts(String categoryStr, List<String> keywords, Integer minPrice, Integer maxPrice, Double minRating, List<String> productsFromSearch)
    {
        List<Product> products = new ArrayList<>();

        for (String productName : productsFromSearch) {
            products.add(storeProducts.get(productName));
        }
        return products.stream()
                .filter(product -> categoryStr == null || product.getCategoryName().equals(categoryStr))
                .filter(product -> keywords == null || keywords.stream().anyMatch(keyword -> product.getDescription().toLowerCase().contains(keyword.toLowerCase())))
                .filter(product -> minPrice == null || product.getPrice() >= minPrice)
                .filter(product -> maxPrice == null || product.getPrice() <= maxPrice)
                .filter(product -> minRating == null || product.getRating() >= minRating)
                .map(Product::getProductName)
                .collect(Collectors.toList());
    }
}
