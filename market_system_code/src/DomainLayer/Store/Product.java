package DomainLayer.Store;

public class Product {
    private String productName;
    private int price;
    private int quantity;

    Product(String productName, int price, int quantity){
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }
}
