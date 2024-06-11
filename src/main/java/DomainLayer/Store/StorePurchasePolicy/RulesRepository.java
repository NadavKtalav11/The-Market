package DomainLayer.Store.StorePurchasePolicy;

import Util.ProductDTO;
import Util.UserDTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;

public enum RulesRepository implements TestRule<UserDTO, List<ProductDTO>> {

    ALCOHOL_RESTRICTION_BELOW_AGE_18((userDTO,products) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy");
        LocalDate birthdate = LocalDate.parse(userDTO.getBirthday(), formatter);
        LocalDate today = LocalDate.now();
        int age = Period.between(birthdate, today).getYears();
        boolean isAlcohol = false;
        for(ProductDTO product: products)
        {
            if(product.getCategoryStr().equals("ALCOHOL")) {
                isAlcohol = true;
                break;
            }
        }
        return age > 18 || (age < 18 && !isAlcohol);
    }),
    ALCOHOL_RESTRICTION_AFTER_2300((userDTO,products) -> {
        LocalTime time = LocalTime.now();
        LocalTime targetTime = LocalTime.of(23, 0);
        boolean isAlcohol = false;
        for(ProductDTO product: products)
        {
            if(product.getCategoryStr().equals("ALCOHOL")) {
                isAlcohol = true;
                break;
            }
        }
        return (time.isAfter(targetTime) && !isAlcohol) || time.isBefore(targetTime);
    }),
    BASKET_CONTAINS_LESS_THAN_5KG_TOMATOS((userDTO,products) -> {
        int quantity = 0;
        for(ProductDTO product: products)
        {
            if(product.getName().equals("tomato")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity < 5;
    }),
    NO_ICE_CREAM_IN_ROSH_HODESH((userDTO,products) -> {
        boolean isIceCream = false;
        for(ProductDTO product: products)
        {
            if(product.getName().equals("ice cream")) {
                isIceCream = true;
                break;
            }
        }
        if(isIceCream) {
            LocalDate today = LocalDate.now();
            return today.getDayOfMonth() != 1;
        }
        return true;
    }),
    BASKET_CONTAINS_AT_LEAST_2_CORNS((userDTO,products) -> {
        int quantity = 0;
        for(ProductDTO product: products)
        {
            if(product.getName().equals("corn")) {
                quantity += product.getQuantity();
                break;
            }
        }
        return quantity >= 2;
    }),
    BASKET_CONTAINS_EGGPLANTS((userDTO,products) -> {
        for(ProductDTO product: products)
        {
            if(product.getName().equals("eggplants")) {
                return true;
            }
        }
        return false;
    }),
    IS_HOLIDAY_EVENING((userDTO, products) -> {
        Set<LocalDate> holidays = new HashSet<>();
        int year = LocalDate.now().getYear();
        // Adding fixed-date holidays
        holidays.add(LocalDate.of(year, 12, 25)); // Christmas
        holidays.add(LocalDate.of(year, 1, 6)); // Epiphany
        holidays.add(LocalDate.of(year, 11, 1)); // All Saints' Day
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        return holidays.contains(tomorrow);
    });

    private final BiPredicate<UserDTO, List<ProductDTO>> predicate;

    RulesRepository(BiPredicate<UserDTO, List<ProductDTO>> predicate) {
        this.predicate = predicate;
    }

    @Override
    public BiPredicate<UserDTO, List<ProductDTO>> getPredicate() {
        return predicate;
    }

}
