package DomainLayer.PaymentServices;


public class ProductDetail {
    private String productName;
    private int amount;

    public ProductDetail(String productName, int amount) {
        this.productName = productName;
        this.amount = amount;
    }

    // Getters and Setters
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
