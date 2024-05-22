package DomainLayer.Store;

public enum Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    HOME,
    BOOKS,
    TOYS;

    public static Category fromString(String categoryStr) {
        try {
            return Category.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // or handle the error as appropriate
        }
    }
}
