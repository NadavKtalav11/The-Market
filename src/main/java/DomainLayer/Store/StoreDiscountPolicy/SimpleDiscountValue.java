package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;
import java.util.function.Predicate;

public class SimpleDiscountValue implements DiscountValue {

    private final int percentage;
    private final Category category;
    private final boolean isStoreDiscount;
    private final List<String> productsNames;
    private final Object productsNamesLock;
    private final Object categoryLock;
    private final Object isStoreDiscountLock;
    private final Object percentageLock;


    public SimpleDiscountValue(int percentage, Category category, boolean isStoreDiscount, List<String> productsNames) {
        this.percentage = percentage;
        this.category = category;
        this.isStoreDiscount = isStoreDiscount;
        this.productsNames = productsNames;
        this.productsNamesLock = new Object();
        this.categoryLock = new Object();
        this.isStoreDiscountLock = new Object();
        this.percentageLock = new Object();
    }

    public int calcDiscount(List<ProductDTO> basketProducts) {
        int totalPrice = 0;

        if (isCategoryDiscount()) {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> product.getCategoryStr().equals(getCategory().toString()));
        } else if (isProductsDiscount()) {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> getProductsNames().contains(product.getName()));
        } else {
            totalPrice = calculateTotalPriceByCriterion(basketProducts, product -> true);
        }

        return applyDiscount(totalPrice, getPercentage());
    }

    private int calculateTotalPriceByCriterion(List<ProductDTO> basketProducts, Predicate<ProductDTO> criterion) {
        return basketProducts.stream()
                .filter(criterion)
                .mapToInt(ProductDTO::getPrice)
                .sum();
    }

    private int applyDiscount(int totalPrice, int percentage) {
        return totalPrice * percentage / 100;
    }

    @Override
    public String getDescription() {
        if (isCategoryDiscount()) {
            return "Discount of " + getPercentage() + "% on category " + getCategory().toString();
        } else if (isProductsDiscount()) {
            return "Discount of " + getPercentage() + "% on products " + getProductsNames().toString();
        } else {
            return "Discount of " + getPercentage() + "% on store";
        }
    }

    public int getPercentage() {
        synchronized (percentageLock) {
            return percentage;
        }
    }

    public boolean isStoreDiscount() {
        synchronized (isStoreDiscountLock) {
            return isStoreDiscount;
        }
    }

    private Category getCategory() {
        synchronized (categoryLock) {
            return category;
        }
    }

    private List<String> getProductsNames() {
        synchronized (productsNamesLock) {
            return productsNames;
        }
    }

    public boolean isCategoryDiscount() {
        synchronized (categoryLock) {
            return category != null;
        }
    }

    public boolean isProductsDiscount() {
        synchronized (productsNamesLock) {
            return productsNames != null;
        }
    }
}
