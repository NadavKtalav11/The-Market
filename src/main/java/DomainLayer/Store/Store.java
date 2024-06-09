package DomainLayer.Store;

import Util.ProductDTO;

import java.util.*;
import java.util.ArrayList;
import java.util.stream.Collectors;
import  DomainLayer.Notifications.Notification;
import  DomainLayer.Notifications.StoreNotification;



public class Store {
    private String store_ID;
    private Map<String, Product> storeProducts = new HashMap<String, Product>();
    private boolean isOpened;
    private DiscountPolicy discountPolicy;
    private PurchasePolicy purchasePolicy;
    private double rating;
    private int numOfRatings;
    private Map<String, String> receiptsIdsUserIds; //<receiptId, userId>
    private String storeName;
    private String description;

    private final Object storeProductLock;
    private final Object storeIdLock;
    private final Object isOpenedLock;
    private final Object receiptsLock;

    Store(String store_ID, String storeName, String description)
    {
        this.store_ID = store_ID;
        this.isOpened = true;
        storeIdLock= new Object();
        discountPolicy= new DiscountPolicy();
        purchasePolicy = new PurchasePolicy();
        storeProductLock= new Object();
        isOpenedLock = new Object();
        this.receiptsIdsUserIds = new HashMap<>();
        this.rating = 0;
        this.numOfRatings = 0;
        this.storeName = storeName;
        this.description = description;
        receiptsLock = new Object();
    }

    public String getStoreName(){
        return storeName;
    }

    public void returnProductToStore(Map<String, Integer> products){
        synchronized (storeProductLock) {
            for (String product : products.keySet()) {
                storeProducts.get(product).addToStock(products.get(product));
            }
        }
    }

    public String getStoreID()
    {
        synchronized (storeIdLock) {
            return store_ID;
        }
    }

    public void addProduct(ProductDTO product) {
        Category category = Category.fromString(product.getCategoryStr());
        synchronized (storeProductLock) {
            storeProducts.put(product.getName(), new Product(product, category));
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

    public int calcPriceInStore(String productName, int quantity, String userId)
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

    public void updateProduct(ProductDTO product){
        synchronized (storeProductLock) {
            storeProducts.get(product.getName()).setPrice(product.getPrice());
            storeProducts.get(product.getName()).setQuantity(product.getQuantity());
            storeProducts.get(product.getName()).setDescription(product.getDescription());
            Category category = Category.fromString(product.getCategoryStr());
            storeProducts.get(product.getName()).setCategory(category);
        }
    }

    public void removeProductQuantity(String productName,int quantity){
        synchronized (storeProductLock) {
            Product product = storeProducts.get(productName);
            if (product.getQuantity() > quantity){
                product.setQuantity(product.getQuantity()-quantity);
            }
        }
    }

    public void closeStore()
    {
        synchronized (isOpenedLock) {
            this.isOpened = false;
        }


    }

    public void sendMessageToStaffOfStore(Notification notification) {
//        founder.notifyObserver(notification);
//        for (User u : getOwnersOfStore())
//            u.notifyObserver(notification);
//        for (User u : getManagersOfStore())
//            u.notifyObserver(notification);
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

    public boolean checkDiscountPolicy(String userId, String productName)
    {
        return this.discountPolicy.checkDiscountPolicy(userId, productName);
    }

    public boolean checkPurchasePolicy(String userId, String productName)
    {
        return this.purchasePolicy.checkPurchasePolicy(userId, productName);
    }

    public List<String> matchProducts(String productName, String categoryStr, List<String> keywords)
    {
        synchronized (storeProductLock) {
            List<Product> products = storeProducts.values().stream().toList();
            return products.stream()
                    .filter(product -> productName == null || product.getProductName().toLowerCase().contains(productName.toLowerCase()))
                    .filter(product -> categoryStr == null || product.getCategoryName().equalsIgnoreCase(categoryStr))
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

    public void addReceipt(String receiptId, String userId)
    {
        synchronized (receiptId) {
            receiptsIdsUserIds.put(receiptId, userId);
        }
    }
}
