package DomainLayer.PaymentServices;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Receipt {
    private int userId;
    private int totalPrice;
    private LocalDate date;
    private HashMap<Integer, List<ProductDetail>> storeIdAndProductDetails; //<storeId, List<details>>

    public Receipt(int userId, int totalPrice, LocalDate date) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.date = date;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
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
