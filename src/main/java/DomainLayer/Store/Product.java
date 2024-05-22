package DomainLayer.Store;

public class Product {
    private String productName;
    private int price;
    private int quantity;
    private double rating;
    private int numOfRatings;
    private Category category;
    private String description;


    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String productName, int price, int quantity, String description, Category category){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.rating = 0;
        this.numOfRatings = 0;
        this.description = description;
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

    public String getProductName() {
        return productName;
    }
}
