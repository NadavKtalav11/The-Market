package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ExceptionsEnum;
import Util.ProductDTO;
import Util.TestRuleDTO;
import Util.UserDTO;

import java.util.List;

public class SimpleRule implements Rule {
    private TestRule rule;
    protected final Object ruleLock;

    public SimpleRule(TestRuleDTO rule) {
        ruleLock = new Object();
        switch (rule.getType()) {
            case "Time":
                this.rule = new TimeRule(rule.getTime(), rule.getRange(), Category.fromString(rule.getCategory()), rule.getProductName(), rule.getDescription(), rule.isContains());
                break;
            case "Price":
                this.rule = new PriceRule(rule.getPrice(), rule.getRange(), Category.fromString(rule.getCategory()), rule.getProductName(), rule.getDescription(), rule.isContains());
                break;
            case "Amount":
                this.rule = new AmountRule(rule.getQuantity(), rule.getRange(), Category.fromString(rule.getCategory()), rule.getProductName(), rule.getDescription(), rule.isContains());
                break;
            case "Age":
                this.rule = new AgeRule(rule.getAge(), rule.getRange(), Category.fromString(rule.getCategory()), rule.getProductName(), rule.getDescription(), rule.isContains());
                break;
            case "Date":
                this.rule = new DateRule(rule.getDate(), rule.getRange(), Category.fromString(rule.getCategory()), rule.getProductName(), rule.getDescription(), rule.isContains());
                break;
            default:
                throw new IllegalArgumentException(ExceptionsEnum.InvalidRuleType.toString());
        }
    }

    public String getDescription() {
        synchronized (ruleLock) {
            return rule.getDescription();
        }
    }

    @Override
    public boolean checkRule(UserDTO user, List<ProductDTO> products) {
        synchronized (ruleLock) {
            return rule.test(user, products);
        }
    }
}
