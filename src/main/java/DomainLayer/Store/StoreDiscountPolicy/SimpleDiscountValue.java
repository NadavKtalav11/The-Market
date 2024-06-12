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

        public int calcDiscount() {
            //todo implement this
            return -1;
        }
}
