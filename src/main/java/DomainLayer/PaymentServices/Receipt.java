package DomainLayer.PaymentServices;

import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private int receiptId;
    private int storeId;
    private int userId;
    private Map<String, Integer> productList = new HashMap<>(); //<productName, price>

    public Receipt(int receiptId, int storeId, int userId, Map<String, Integer> productList)
    {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.userId = userId;
        this. productList = productList;
    }

    public int getTotalPriceOfStoreReceipt()
    {
        int storePrice = 0;
        for (String productName : productList.keySet()) {
            storePrice += productList.get(productName);
        }
        return storePrice;
    }

    public int getStoreId()
    {
        return storeId;
    }

    public int getReceiptId() {
        return receiptId;
    }
}
