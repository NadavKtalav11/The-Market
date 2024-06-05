package Util;

import java.util.Map;

public class CartDTO {
    String userID;
    int cartPrice;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getCartPrice() {
        return cartPrice;
    }

    public void setCartPrice(int cartPrice) {
        this.cartPrice = cartPrice;
    }

    public Map<String, Map<String, Integer>> getStoreToProducts() {
        return storeToProducts;
    }

    public void setStoreToProducts(Map<String, Map<String, Integer>> storeToProducts) {
        this.storeToProducts = storeToProducts;
    }

    Map<String, Map<String, Integer>> storeToProducts; //Map<storeID, Map<productName, quantity>>

    public CartDTO (String userID, int cartPrice, Map<String, Map<String, Integer>> storeToProducts){
        this.userID = userID;
        this.cartPrice = cartPrice;
        this.storeToProducts = storeToProducts;
    }


}
