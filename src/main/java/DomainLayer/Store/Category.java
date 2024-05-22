package DomainLayer.Store;

public enum Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    HOME,
    BOOKS,
    TOYS;

    public static Category fromString(String categoryStr) {
        return Category.valueOf(categoryStr.toUpperCase());
    }
}
