package fr.maximouz.thepit.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class Format {

    private static final DecimalFormat format = new DecimalFormat("#,###.#", new DecimalFormatSymbols(Locale.GERMAN));

    public static String format(double value) {
        return format.format(value);
    }

    public static double round(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
    }

    public static int roundToInt(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN).intValue();
    }

}
