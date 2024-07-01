package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TimeRule extends TestRule {
    private LocalTime time;

    public TimeRule(LocalTime time, String range, Category category, String productName, String description, boolean contains) {
        super(range, category, productName, description, contains);
        this.time = time;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        Clock clock = TestRule.getClock();
        LocalTime currentTime = LocalTime.now(clock);
        boolean timeCheck = this.checkRange(range, currentTime, time);

        if(category !=null || productName != null) {
            if(timeCheck) {
                if (contains)
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            }
            else
                return true;
        }
        return timeCheck;
    }

    public boolean checkRange(String range, LocalTime currentTime, LocalTime time) {
        switch (range) {
            case "Above":
                return currentTime.isAfter(time);
            case "Below":
                return currentTime.isBefore(time);
            case "Exact":
                return currentTime.equals(time);
            default:
                throw new IllegalArgumentException("Invalid range type");
        }
    }
}
