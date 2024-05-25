package DomainLayer.PaymentServices;

import java.time.LocalDate;
import java.util.*;

public class Receipt {
    private int receiptId;
    private int userId;
    private int totalPrice;
    private String userName;
    private int creditCardNumber;
    private int cvv;
    private int month;
    private int year;
    private Date date;
    Map<Integer, Map<String, Integer>> productList = new HashMap<>();
    private HashMap<Integer, List<ProductDetail>> storeIdAndProductDetails; //<storeId, List<details>>

    public Receipt(int receiptId, int userId, int totalPrice,String userName,
                   int creditCardNumber,int cvv, int month, int year, Map<Integer, Map<String, Integer>> productList) {
        this.receiptId = receiptId;
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.userName = userName;
        this.creditCardNumber= creditCardNumber;
        this.cvv = cvv;
        this.month=month;
        this.year=year;
        this.productList = productList;
        this.date = new Date(); // Current date and time
        this.storeIdAndProductDetails = new HashMap<>();
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



    public Map<Integer, List<ProductDetail>> getStoreIdAndProductDetails() {
        return storeIdAndProductDetails;
    }

    public void addProduct(int storeId, String productName, int amount, int price) {
        if (storeIdAndProductDetails.containsKey(storeId))
            this.storeIdAndProductDetails.get(storeId).add(new ProductDetail(productName, amount, price));
        else
        {
            List<ProductDetail> productDetailList = new ArrayList<>();
            productDetailList.add(new ProductDetail(productName, amount, price));
            this.storeIdAndProductDetails.put(storeId, productDetailList);
        }
    }


    public int getTotalPriceOfStoreInReceipt(int storeId)
    {
        int storePrice = 0;
        for (ProductDetail productDetail : storeIdAndProductDetails.get(storeId)) {
            storePrice += productDetail.getPrice();
        }
        return storePrice;
    }
}
