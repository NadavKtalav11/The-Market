package DomainLayer.PaymentServices;

import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private String receiptId;
    private String storeId;
    private String userId;
    private Map<String, Integer> productList = new HashMap<>(); //<productName, price>

    private final Object productListLock;

    public Receipt(String receiptId, String storeId, String userId, Map<String, Integer> productList)
    {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.userId = userId;
        this. productList = productList;
        productListLock= new Object();
    }

    public int getTotalPriceOfStoreReceipt()
    {
        synchronized (productListLock) {
            int storePrice = 0;
            for (String productName : productList.keySet()) {
                storePrice += productList.get(productName);
            }
            return storePrice;
        }
    }

    public String getStoreId()
    {
        return storeId;
    }

    public String getReceiptId() {
        return receiptId;
    }
}
