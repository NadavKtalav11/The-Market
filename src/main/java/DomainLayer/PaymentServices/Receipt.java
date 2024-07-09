package DomainLayer.PaymentServices;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "store_id") // Specify the column name explicitly
    private String storeId;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "receipt_id", referencedColumnName = "receipt_id"),
            @JoinColumn(name = "store_id", referencedColumnName = "store_id")
    })
    private List<ProductDetailReceipt> productList = new ArrayList<>();

    @Transient
    private Object productListLock = new Object();

    public Receipt(String receiptId, String storeId, String userId, List<ProductDetailReceipt> productList) {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.userId = userId;
        this.productList = productList;
    }

    public Receipt() {
        productListLock = new Object();
    }

    public int getTotalPriceOfStoreReceipt() {
        synchronized (productListLock) {
            int storePrice = 0;
            for (ProductDetailReceipt product : productList) {
                storePrice += product.getPrice();
            }
            return storePrice;
        }
    }

    public String getStoreId() {
        return storeId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getUserId() {
        return userId;
    }

    public List<ProductDetailReceipt> getProductList() {
        return productList;
    }

    public Map<String, List<Integer>> getProductListToMap() {
        Map<String, List<Integer>> productsList = new HashMap<>();
        for (ProductDetailReceipt product : productList) {
            List<Integer> quantityAndPrice = new ArrayList<>();
            quantityAndPrice.add(product.getAmount());
            quantityAndPrice.add(product.getPrice());
            productsList.put(product.getId().getProductName(), quantityAndPrice);
        }
        return productsList;
    }
}
