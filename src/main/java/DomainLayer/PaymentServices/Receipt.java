package DomainLayer.PaymentServices;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Receipt {
    private int userId;
    private int totalPrice;
    private LocalDate date;
    private HashMap<Integer, ProductDetail> priceAndProductNameAndAmount;

    public Receipt(int userId, int totalPrice, LocalDate date) {
        this.userId = userId;
        this.totalPrice = totalPrice;
        this.date = date;
        this.priceAndProductNameAndAmount = new HashMap<>();
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

    public Map<Integer, ProductDetail> getPriceAndProductNameAndAmount() {
        return priceAndProductNameAndAmount;
    }

    public void addProduct(int price, String productName, int amount) {
        this.priceAndProductNameAndAmount.put(price, new ProductDetail(productName, amount));
    }
}
