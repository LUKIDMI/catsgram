package ru.yandex.practicum.catsgram.model.enums;

public enum SortOrder {
    ASCENDING, DESCENDING;

    public static SortOrder from(String order) {
        return switch (order.toLowerCase()) {
            case "asc", "ascending" -> SortOrder.ASCENDING;
            case "desc", "descending" -> SortOrder.DESCENDING;
            default -> null;
        };
    }
}