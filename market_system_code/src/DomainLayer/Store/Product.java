package DomainLayer.Store;

public class Product {
    private String productName;

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    private int price;
    private int quantity;

    Product(String productName, int price, int quantity){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
