package com.example.assetsview;

import java.text.DecimalFormat;

public class Utils {

    public static String parseAmount(String amount) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Double.parseDouble(amount));
    }

    public static String parseDate(String dateString, String tag) {
        return dateString;
    }
}
