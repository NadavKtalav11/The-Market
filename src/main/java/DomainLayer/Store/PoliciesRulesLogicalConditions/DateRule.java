package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.time.LocalDate;
import java.util.List;

public class DateRule extends TestRule {
    private LocalDate date;

    public DateRule(LocalDate date, String range, Category category, String productName, String description, boolean contains) {
        super(range, category, productName, description, contains);
        this.date = date;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        LocalDate currentDate = LocalDate.now();
        boolean dateCheck = this.checkRange(range, currentDate, date);

        if(category !=null || productName != null) {
            if(dateCheck) {
                if (contains)
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            }
            else
                return true;
        }
        return dateCheck;
    }

    public boolean checkRange(String range, LocalDate currentDate, LocalDate date) {
        switch (range) {
            case "Above":
                return currentDate.isAfter(date);
            case "Below":
                return currentDate.isBefore(date);
            case "Exact":
                return currentDate.equals(date);
            default:
                throw new IllegalArgumentException("Invalid range type");
        }
    }
}
