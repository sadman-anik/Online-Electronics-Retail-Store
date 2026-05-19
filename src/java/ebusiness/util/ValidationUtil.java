package ebusiness.util;

public class ValidationUtil {

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static boolean isPositive(Integer value) {
        return value != null && value > 0;
    }

    public static boolean isPositive(Double value) {
        return value != null && value > 0;
    }

    public static boolean isStrongPassword(String value) {
        return value != null
                && value.length() >= 8
                && value.matches(".*[A-Z].*")
                && value.matches(".*[a-z].*")
                && value.matches(".*[0-9].*")
                && value.matches(".*[^A-Za-z0-9].*");
    }

    public static String trimToEmpty(String value) {
        return value == null ? "" : value.trim();
    }
}
