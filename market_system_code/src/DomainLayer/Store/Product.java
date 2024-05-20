package DomainLayer.Store;

public class Product {
    private int productId;
    private String productName;
    private int price;
    private int quantity;

    public Product(String productName, int price, int quantity, int productId){

        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    public int getProductId()
    {
        return this.productId;
    }
    public int getQuantity()
    {
        return this.quantity;
    }
}
