package Util;

import DomainLayer.Store.Category;

import java.util.List;

public class DiscountValueDTO {

    private final int percentage;
    private final String category;
    private final boolean isStoreDiscount;
    private final List<String> productsNames;

    public DiscountValueDTO(int percentage, String category, boolean isStoreDiscount, List<String> productsNames) {
        this.percentage = percentage;
        this.category = category;
        this.isStoreDiscount = isStoreDiscount;
        this.productsNames = productsNames;
    }

    public int getPercentage() {
        return percentage;
    }

    public String getCategory() {
        return category;
    }

    public boolean getIsStoreDiscount() {
        return isStoreDiscount;
    }

    public List<String> getProductsNames() {
        return productsNames;
    }
}
