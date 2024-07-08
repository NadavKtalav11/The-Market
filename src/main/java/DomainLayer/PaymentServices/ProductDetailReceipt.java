package DomainLayer.PaymentServices;

import jakarta.persistence.*;

@Entity
@Table(name = "product_detail_receipt")
public class ProductDetailReceipt {

    @EmbeddedId
    private ProductDetailReceiptId productDetailReceiptId;

    @Column(name = "amount")
    private int amount;

    @Column(name = "price")
    private int price;

    public ProductDetailReceipt(ProductDetailReceiptId id, int amount, int price) {
        this.productDetailReceiptId = id;
        this.amount = amount;
        this.price = price;
    }

    public ProductDetailReceipt() {
    }

    // Getters and Setters
    public ProductDetailReceiptId getId() {
        return productDetailReceiptId;
    }

    public void setId(ProductDetailReceiptId id) {
        this.productDetailReceiptId = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getPrice() {
        return price;
    }
}
