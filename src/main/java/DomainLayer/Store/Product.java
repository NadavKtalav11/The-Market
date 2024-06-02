package DomainLayer.Store;

import Util.ProductDTO;

public class Product {
    private String productName;
    private int price;
    private int quantity;
    private double rating;
    private int numOfRatings;
    private Category category;
    private String description;

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(ProductDTO product, Category category){
        this.productName = product.getName();
        this.price = product.getPrice();
        this.quantity = product.getQuantity();
        this.rating = 0;
        this.numOfRatings = 0;
        this.description = product.getDescription();
        this.category = category;
    }

    public int getQuantity()
    {
        return this.quantity;
    }

    public int getPrice()
    {
        return this.price;
    }

    public String getProductName() {
        return this.productName;
    }

    public String getCategoryName() {
        return this.category.toString();
    }

    public double getRating() {
        return this.rating;
    }

    public int getNumOfRatings() {
        return this.numOfRatings;
    }

    public String getDescription() {
        return this.description;
    }

    public void addToStock(int amount){
        this.quantity = quantity+amount;
    }


}
