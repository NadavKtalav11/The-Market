package DomainLayer.PaymentServices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt {
    private String receiptId;
    private String storeId;
    private String userId;
    private Map<String, List<Integer>> productList = new HashMap<>(); //<productName, price>

    private final Object productListLock;

    public Receipt(String receiptId, String storeId, String userId, Map<String, List<Integer>> productList)
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
                storePrice += productList.get(productName).get(1);
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

    public String getUserId() {
        return userId;
    }

    public Map<String, List<Integer>> getProductList() {
        return productList;
    }
}
