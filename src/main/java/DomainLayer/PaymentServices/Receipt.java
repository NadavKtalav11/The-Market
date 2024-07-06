package DomainLayer.PaymentServices;

import jakarta.persistence.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "receipt")
public class Receipt {

    @Id
    private String receiptId;

    @Column(name = "store_id") // Specify the column name explicitly
    private String storeId;

    private String userId;

    @Transient
    private Map<String, List<Integer>> productList = new HashMap<>(); //<productName, {quantity, price}>

    @Transient
    private Object productListLock = new Object();

    public Receipt(String receiptId, String storeId, String userId, Map<String, List<Integer>> productList) {
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
            for (String productName : productList.keySet()) {
                storePrice += productList.get(productName).get(1);
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

    public Map<String, List<Integer>> getProductList() {
        return productList;
    }
}
