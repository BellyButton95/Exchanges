package Utils;

public class NumberUtils {

    public static boolean isValidQty(double qty) {
        return !Double.isNaN(qty) && Double.isFinite(qty) && qty > 0;
    }

    public static boolean isValidQtyInclNegative(double qty) {
        return !Double.isNaN(qty) && Double.isFinite(qty) && qty != 0;
    }

    public static boolean isValidPrice(double price) { return !Double.isNaN(price) && Double.isFinite(price) && price > 0; }
}
