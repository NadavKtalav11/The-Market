package DomainLayer.PaymentServices;

import Util.PaymentDTO;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "acquisition")
public class Acquisition {
    @Id
    private String acquisitionId;
    private String userId;
    private int totalPrice;
    private String holderId;
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;
    private Date date;

    // One-to-many relationship with Receipt
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "acquisition_id") // This will create an acquisition_id column in the Receipt table
    @MapKeyColumn(name = "store_id") // Column in the Receipt table for store_id
    private Map<String, Receipt> storeIdAndReceipt= new HashMap<>(); //<storeId, Receipt>

    @Transient
    private final Object storeReceiptLock;

    public Acquisition(String acquisitionId, String userId, int totalPrice, PaymentDTO payment, Map<String, Map<String, List<Integer>>> productList) {
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.holderId = payment.getHolderId();
        this.creditCardNumber= payment.getCreditCardNumber();
        storeReceiptLock = new Object();
        this.cvv = payment.getCvv();
        this.month=payment.getMonth();
        this.year= payment.getYear();
        this.date = new Date(); // Current date and time

        for (String storeId : productList.keySet())
        {
            storeIdAndReceipt.put(storeId, new Receipt(getNewReceiptId(), storeId, userId, productList.get(storeId)));

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

    public Map<String, Receipt> getStoreIdAndReceipt()
    {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt;
        }
    }

    public int getTotalPriceOfStoreInAcquisition(String storeId)
    {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt.get(storeId).getTotalPriceOfStoreReceipt();
        }
    }

    public String getReceiptIdByStoreId(String storeId)
    {
        synchronized (storeReceiptLock) {
            return storeIdAndReceipt.get(storeId).getReceiptId();
        }
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

    public String getNewReceiptId(){
        UUID uuid = UUID.randomUUID();
        String id = "receipt-"+uuid.toString() ;
        return id;
    }
}
