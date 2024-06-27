package Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReceiptDTO {

    private String receiptId;
    private String storeId;
    private String userId;
    private Map<String, List<Integer>> productList = new HashMap<>(); //<productName, price>

    public ReceiptDTO(String receiptId, String storeId, String userId, Map<String, List<Integer>> productList)
    {
        this.receiptId = receiptId;
        this.storeId = storeId;
        this.userId = userId;
        this. productList = productList;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public String getStoreId() {
        return storeId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, List<Integer>> getProductList() {
        return productList;
    }
}
