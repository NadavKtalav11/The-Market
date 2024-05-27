package DomainLayer.PaymentServices;

import java.util.*;

public class Acquisition {
    private int acquisitionId;
    private int userId;
    private int totalPrice;
    private String holderId;
    private String creditCardNumber;
    private int cvv;
    private int month;
    private int year;
    private Date date;
    private Map<Integer, Receipt> storeIdAndReceipt= new HashMap<>(); //<storeId, Receipt>

    public Acquisition(int acquisitionId, int userId, int totalPrice, String holderId,
                       String creditCardNumber, int cvv, int month, int year, Map<Integer, Map<String, Integer>> productList, int receiptIdCounter) {
        this.acquisitionId = acquisitionId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.holderId = holderId;
        this.creditCardNumber= creditCardNumber;
        this.cvv = cvv;
        this.month=month;
        this.year=year;
        this.date = new Date(); // Current date and time
        for (Integer storeId : productList.keySet())
        {
            storeIdAndReceipt.put(storeId, new Receipt(receiptIdCounter, storeId, userId, productList.get(storeId)));
            receiptIdCounter++;
        }
    }

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
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

    public Map<Integer, Receipt> getStoreIdAndReceipt()
    {
        return storeIdAndReceipt;
    }

    public int getTotalPriceOfStoreInAcquisition(int storeId)
    {
        return storeIdAndReceipt.get(storeId).getTotalPriceOfStoreReceipt();
    }

    public int getReceiptIdByStoreId(int storeId)
    {
        return storeIdAndReceipt.get(storeId).getReceiptId();
    }

    public Map<Integer, Integer> getReceiptIdAndStoreIdMap() {
        Map<Integer, Integer> receiptIdAndStoreIdMap = new HashMap<>();
        for (Map.Entry<Integer, Receipt> entry : storeIdAndReceipt.entrySet()) {
            receiptIdAndStoreIdMap.put(entry.getValue().getReceiptId(), entry.getKey());
        }
        return receiptIdAndStoreIdMap;
    }
}
