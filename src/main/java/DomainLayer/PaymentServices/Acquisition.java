package DomainLayer.PaymentServices;

import java.util.*;

public class Acquisition {
    private String acquisitionId;
    private String userId;
    private int totalPrice;
    private String holderId;
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;
    private Date date;
    private Map<String, Receipt> storeIdAndReceipt= new HashMap<>(); //<storeId, Receipt>

    private final Object storeReceiptLock;

    public Acquisition(String acquisitionId, String userId, int totalPrice, String holderId,
                       String creditCardNumber, int cvv, int month, int year, Map<String, Map<String, Integer>> productList, String receiptIdCounter) {
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.holderId = holderId;
        this.creditCardNumber= creditCardNumber;
        storeReceiptLock = new Object();
        this.cvv = cvv;
        this.month=month;
        this.year=year;
        this.date = new Date(); // Current date and time

        for (String storeId : productList.keySet())
        {
            storeIdAndReceipt.put(storeId, new Receipt(receiptIdCounter, storeId, userId, productList.get(storeId)));

        }
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
}
