package DomainLayer.Store.PoliciesRulesLogicalConditions;

import DomainLayer.Store.Category;
import Util.ProductDTO;
import Util.UserDTO;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class AgeRule extends TestRule {
    private int age;
    private static final ThreadLocal<Clock> clock = ThreadLocal.withInitial(Clock::systemDefaultZone); // Default clock

    public AgeRule(int age, String range, Category category, String productName, String description, boolean contains){
        super(range, category, productName, description, contains);
        this.age = age;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthdate = LocalDate.parse(user.getBirthday(), formatter);
        LocalDate today = LocalDate.now(clock.get());
        int userAge = Period.between(birthdate, today).getYears();

        //check if the user is above or below the age, age can be "Above" or "Below" or "Exactly"
        boolean ageCheck = checkRange(range, userAge, age);

        if (ageCheck)
            return isRuleSatisfied(products,-1);
        else
            return true;
    }

    public static void setClock(Clock newClock) {
        clock.set(newClock);
    }

    public static Clock getClock() {
        return clock.get();
    }
}
