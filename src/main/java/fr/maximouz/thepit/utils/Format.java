package fr.maximouz.thepit.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class Format {

    private static final DecimalFormat format = new DecimalFormat("#,###.#", new DecimalFormatSymbols(Locale.US));

    public static String format(double value) {
        return format.format(value);
    }

    public static String format(BigDecimal value) {
        return format.format(value);
    }

    public static int roundToInt(double value) {
        return new BigDecimal(value).setScale(2, BigDecimal.ROUND_HALF_EVEN).intValue();
    }

    public static String time(long ms) {
        if (ms <= 0)
            return "0s";
        return TimeUnit.MILLISECONDS.toDays(ms) > 0
                ? new SimpleDateFormat("dd:HH").format(ms).replace(":", "j") + "h"
                : TimeUnit.MILLISECONDS.toHours(ms) > 0
                    ? new SimpleDateFormat("HH:mm").format(ms).replace(":", "h") + "m"
                    : TimeUnit.MILLISECONDS.toMinutes(ms) > 0
                        ? new SimpleDateFormat("mm:ss").format(ms).replace(":", "m") + "s"
                        : TimeUnit.MILLISECONDS.toSeconds(ms) > 0
                            ? TimeUnit.MILLISECONDS.toSeconds(ms) + "s"
                            : "0." + ms + "s";
    }

}
