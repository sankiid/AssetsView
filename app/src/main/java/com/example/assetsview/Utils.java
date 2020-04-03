package com.example.assetsview;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Utils {

    public static String parseAmount(String amount) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(Double.parseDouble(amount));
    }

    public static long parseDate(String dateString) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
        try {
            return format.parse(dateString).getTime();
        } catch (ParseException e) {
        }
        return 0L;
    }

    public static String format(long time, String format) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.US);
        return sdf.format(c.getTime());
    }
}
