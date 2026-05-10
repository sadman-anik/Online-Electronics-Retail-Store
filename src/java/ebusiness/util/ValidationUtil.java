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
}
