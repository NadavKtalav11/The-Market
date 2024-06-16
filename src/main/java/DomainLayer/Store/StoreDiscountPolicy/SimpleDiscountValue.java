package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.List;

public class SimpleDiscountValue implements DiscountValue {

        private final int percentage;
        private final Category category;
        private final boolean isStoreDiscount;
        private final List<String> productsNames;

        public SimpleDiscountValue(int percentage, Category category, boolean isStoreDiscount, List<String> productsNames) {
            this.percentage = percentage;
            this.category = category;
            this.isStoreDiscount = isStoreDiscount;
            this.productsNames = productsNames;
        }

        public int calcDiscount(List<ProductDTO> basketProducts) {
            if (isCategoryDiscount()) { //category != null && productsNames == null && !isStoreDiscount
                //loop over basketProducts and check if there is a product from the category
                int totalPrice = 0;
                for (ProductDTO product : basketProducts) {
                    if (product.getCategoryStr().equals(category.toString())) {
                        totalPrice+=product.getPrice();
                    }
                }
                return totalPrice * percentage / 100;
            }
            else if (isProductsDiscount()) { //category == null && productsNames != null && !isStoreDiscount
                //loop over productsNames and discount the total price of the products
                int totalPrice = 0;
                for (ProductDTO product : basketProducts) {
                    if (productsNames.contains(product.getName())) {
                        totalPrice+=product.getPrice();
                    }
                }
                return totalPrice * percentage / 100;
            }
            else { //store discount
                int totalPrice = 0;
                for (ProductDTO product : basketProducts) {
                    totalPrice+=product.getPrice();
                }
                return totalPrice * percentage / 100;
            }
        }

    @Override
    public String getDescription() {
        if (isCategoryDiscount()) {
            return "Discount of " + percentage + "% on category " + category.toString();
        }
        else if (isProductsDiscount()) {
            return "Discount of " + percentage + "% on products " + productsNames.toString();
        }
        else {
            return "Discount of " + percentage + "% on store";
        }
    }

    public int getPercentage() {
            return percentage;
        }

        public boolean isStoreDiscount() {
            return isStoreDiscount;
        }

        public boolean isCategoryDiscount() {
            return category != null;
        }

        public boolean isProductsDiscount() {
            return productsNames != null;
        }
}
