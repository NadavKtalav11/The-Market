package DomainLayer.Store;

import Util.UserDTO;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiPredicate;

public enum RulesRepository implements TestRule<UserDTO, String>{

    ALCOHOL_RESTRICTION_BELOW_AGE_18((userDTO,productsMap) -> {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy");
        LocalDate birthdate = LocalDate.parse(userDTO.getBirthday(), formatter);
        LocalDate today = LocalDate.now();
        int age = Period.between(birthdate, today).getYears();
        return age > 18 ;//|| (age < 18 && !productsList.contains());
    }),

    BASKET_CONTAINS_LESS_THAN_5KG_TOMATOS((userDTO,productsMap) -> {
        Integer quantity1 = productsMap.get("tomato") != null ? productsMap.get("tomato").get(0) : 0;
        Integer quantity2 = productsMap.get("Tomato") != null ? productsMap.get("Tomato").get(0) : 0;
        return quantity1 + quantity2 < 5;
    }),
    NO_ICE_CREAM_IN_ROSH_HODESH((userDTO,productsMap) -> {
        if(productsMap.get("ice cream") != null) {
            LocalDate today = LocalDate.now();
            return today.getDayOfMonth() != 1;
        }
        return true;
    }),
    BASKET_CONTAINS_AT_LEAST_2_CORNS((userDTO,productsMap) -> {
        Integer quantity = productsMap.get("corn") != null ? productsMap.get("corn").get(0) : 0;
        return quantity >= 2;
    }),
    BASKET_CONTAINS_EGGPLANTS((userDTO,productsMap) -> productsMap.containsKey("eggplant")),
    IS_HOLIDAY_EVENING((userDTO, stringListMap) -> {
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

    private final BiPredicate<UserDTO, Map<String, List<Integer>>> predicate;

    RulesRepository(BiPredicate<UserDTO, Map<String, List<Integer>>> predicate) {
        this.predicate = predicate;
    }

    @Override
    public BiPredicate<UserDTO, Map<String, List<Integer>>> getPredicate() {
        return predicate;
    }

}
