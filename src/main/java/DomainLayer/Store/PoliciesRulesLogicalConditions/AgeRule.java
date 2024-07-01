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

    public AgeRule(int age, String range, Category category, String productName, String description, boolean contains){
        super(range, category, productName, description, contains);
        this.age = age;
    }

    @Override
    public boolean test(UserDTO user, List<ProductDTO> products) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
        LocalDate birthdate = LocalDate.parse(user.getBirthday(), formatter);
        LocalDate today = LocalDate.now(getClock());
        int userAge = Period.between(birthdate, today).getYears();

        //check if the user is above or below the age, age can be "Above" or "Below" or "Exactly"
        boolean ageCheck = checkRange(range, userAge, age);

        if(category !=null || productName != null) {
            if(ageCheck) {
                if (contains)
                    return getQuantity(products) > 0;
                else
                    return getQuantity(products) == 0;
            }
            else
                return true;
        }
        return ageCheck;
    }
}
