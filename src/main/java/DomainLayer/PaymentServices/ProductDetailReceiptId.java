package DomainLayer.PaymentServices;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ProductDetailReceiptId implements Serializable {

    private String store_id;

    private String productName;

    // Update constructor, getters, and setters accordingly
    public ProductDetailReceiptId(String store_id, String productName) {
        this.store_id = store_id;
        this.productName = productName;
    }

    public ProductDetailReceiptId() {

    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

}
