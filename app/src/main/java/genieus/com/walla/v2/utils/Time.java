package genieus.com.walla.v2.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by anesu on 8/24/17.
 */

public class Time {
    public static String getDateTimeString(Calendar date){
        final SimpleDateFormat format1 = new SimpleDateFormat("MMM d, h:mm aaa");
        final SimpleDateFormat format2 = new SimpleDateFormat("h:mm aaa");
        final Calendar now = Calendar.getInstance();

        if (date.get(Calendar.YEAR) == now.get(Calendar.YEAR)
                && date.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            final int diff = date.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
            final String day;

            if (diff == 0) {
                day = "Today, " + format2.format(date.getTime());
            } else if (diff == 1) {
                day = "Tomorrow, " + format2.format(date.getTime());
            } else {
                day = format1.format(date.getTime());
            }

            return day;
        } else {
            return format1.format(date.getTime());
        }
    }

}
