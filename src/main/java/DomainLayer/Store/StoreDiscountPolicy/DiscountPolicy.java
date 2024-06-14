package DomainLayer.Store.StoreDiscountPolicy;

import DomainLayer.Store.Category;
import DomainLayer.Store.PoliciesRulesLogicalConditions.Rule;
import Util.DiscountValueDTO;
import Util.ProductDTO;
import Util.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class DiscountPolicy {
    private List<Discount> discountRules;

    private final Object userIdLock;
    private final Object productNameLock;

    public DiscountPolicy()
    {
        List<DiscountValue> discountValue = new ArrayList<>();
        discountValue.add(new SimpleDiscountValue(0, null, true, null)); //Default is 0 percentage on the store
        this.discountRules = new ArrayList<>();
        discountRules.add(new Discount(discountValue, new ArrayList<>()));
        userIdLock = new Object();
        productNameLock = new Object();
    }

    public boolean checkDiscountPolicy(String userId, String productName)
    {
        //TODO: implement so that it will calculate the total price need to reduce from the basket
        return true;
    }

    public void addCondRule(List<Rule<UserDTO, List<ProductDTO>>> rules, List<String> logicalOperators, List<DiscountValueDTO> discDetails, List<String> numericalOperators) {
        List<DiscountValue> discountValue = new ArrayList<>();
        for (DiscountValueDTO discDetail : discDetails) {
            discountValue.add(new SimpleDiscountValue(discDetail.getPercentage(), Category.fromString(discDetail.getCategory()) , discDetail.getIsStoreDiscount(), discDetail.getProductsNames()));
        }
        discountRules.add(new CondDiscount(discountValue, numericalOperators, rules, logicalOperators));
    }

    public void addSimple(List<DiscountValueDTO> discDetails, List<String> discountValueOperators) {
        //call to Discount constructor
        List<DiscountValue> discountValue = new ArrayList<>();
        for (DiscountValueDTO discDetail : discDetails) {
            discountValue.add(new SimpleDiscountValue(discDetail.getPercentage(), Category.fromString(discDetail.getCategory()) , discDetail.getIsStoreDiscount(), discDetail.getProductsNames()));
        }
        discountRules.add(new Discount(discountValue, discountValueOperators));
    }

    public void removeRule(int ruleNum) {
        discountRules.remove(ruleNum);
    }
}
