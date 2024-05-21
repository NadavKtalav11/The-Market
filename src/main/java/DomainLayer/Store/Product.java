package DomainLayer.Store;

public class Product {
    private String productName;
    private int price;
    private int quantity;

    public void setPrice(int price) {
        this.price = price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product(String productName, int price, int quantity){

        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

//    public int getProductId()
//    {
//        return this.productId;
//    }
    public int getQuantity()
    {
        return this.quantity;
    }
}
