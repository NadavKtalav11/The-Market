package DomainLayer.PaymentServices;

import Util.PaymentDTO;
import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "acquisition")
public class Acquisition {

    @Column(name = "transactionId")
    private int transactionId;

    @Id
    @Column(name = "acquisition_id")
    private String acquisitionId;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "total_price")
    private int totalPrice;

    @Column(name = "holder_id")
    private String holderId;

    @Column(name = "credit_card_number")
    private String creditCardNumber;

    @Column(name = "cvv")
    private int cvv;

    @Column(name = "month")
    private int month;

    @Column(name = "year")
    private int year;

    @Column(name = "date")
    private Date date;

//    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "acquisition_id")
//    @MapKeyColumn(name = "store_id")
    @Transient
    private Map<String, Receipt> storeIdAndReceipt = new HashMap<>();

    @Transient
    private final Object storeReceiptLock;

    public Acquisition(int transactionId , String acquisitionId, String userId, int totalPrice, PaymentDTO payment, Map<String, Map<String, List<Integer>>> productList) {
        this.transactionId = transactionId;
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.holderId = payment.getHolderId();
        this.creditCardNumber = payment.getCreditCardNumber();
        storeReceiptLock = new Object();
        this.cvv = payment.getCvv();
        this.month = payment.getMonth();
        this.year = payment.getYear();
        this.date = new Date(); // Current date and time

        for (String storeId : productList.keySet()) {
            Map<String, List<Integer>> productsList = productList.get(storeId);
            List<ProductDetailReceipt> productDetailReceipts = new ArrayList<>();
            for (String productName : productsList.keySet()) {
                int quantity = productsList.get(productName).get(0);
                int price = productsList.get(productName).get(1);
                ProductDetailReceiptId id = new ProductDetailReceiptId(storeId, productName);
                ProductDetailReceipt productDetailReceipt = new ProductDetailReceipt(id, quantity, price);
                productDetailReceipts.add(productDetailReceipt);
            }
            storeIdAndReceipt.put(storeId, new Receipt(getNewReceiptId(), storeId, userId, productDetailReceipts));
        }
    }

    public Acquisition() {
        this.storeReceiptLock = new Object();
    }

    public String getAcquisitionId() {
        return acquisitionId;
    }

    // Getters and Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDate() {
        return date;
    }

    public Map<String, Receipt> getStoreIdAndReceipt() {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt;
        }
    }

    public int getTotalPriceOfStoreInAcquisition(String storeId) {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt.get(storeId).getTotalPriceOfStoreReceipt();
        }
    }

    public String getReceiptIdByStoreId(String storeId) {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt.get(storeId).getReceiptId();
        }
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Map<String, String> getReceiptIdAndStoreIdMap() {
        Map<String, String> receiptIdAndStoreIdMap = new HashMap<>();
        synchronized (storeReceiptLock) {
            for (Map.Entry<String, Receipt> entry : storeIdAndReceipt.entrySet()) {
                receiptIdAndStoreIdMap.put(entry.getValue().getReceiptId(), entry.getKey());
            }
        }
        return receiptIdAndStoreIdMap;
    }

    public String getNewReceiptId() {
        UUID uuid = UUID.randomUUID();
        return "receipt-" + uuid.toString();
    }
}
