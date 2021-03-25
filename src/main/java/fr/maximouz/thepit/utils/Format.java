package fr.maximouz.thepit.utils;

import java.text.DecimalFormat;

public class Format {

    private static DecimalFormat format = new DecimalFormat("#,###.#");

    public static String format(double value) {
        return format.format(value);
    }

}
