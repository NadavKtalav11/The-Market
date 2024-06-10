package DomainLayer.Store;

public enum Category {
    ELECTRONICS,
    CLOTHING,
    FOOD,
    HOME,
    BOOKS,
    TOYS,
    ALCOHOL,
    DAIRY;

    public static Category fromString(String categoryStr) {
        if (categoryStr == null)
            return null;
        try {
            return Category.valueOf(categoryStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null; // or handle the error as appropriate
        }
    }
}
