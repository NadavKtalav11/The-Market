package DomainLayer.Store;

import Util.UserDTO;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yy");
        LocalDate birthdate = LocalDate.parse(userDTO.getBirthday(), formatter);
        LocalDate today = LocalDate.now();
        int age = Period.between(birthdate, today).getYears();
        return age > 18 ;//|| (age < 18 && !productsList.contains());
    });

    //NAME_STARTS_WITH_B(UserDTO -> UserDTO.getName().startsWith("B"));

    private final BiPredicate<UserDTO, Map<String, List<Integer>>> predicate;

    RulesRepository(BiPredicate<UserDTO, Map<String, List<Integer>>> predicate) {
        this.predicate = predicate;
    }

    @Override
    public BiPredicate<UserDTO, Map<String, List<Integer>>> getPredicate() {
        return predicate;
    }

}
