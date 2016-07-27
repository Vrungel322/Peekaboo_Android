package com.peekaboo.utils;

import android.content.Context;
import android.text.format.Time;

import com.peekaboo.R;

import java.text.SimpleDateFormat;

/**
 * Created by st1ch on 21.07.2016.
 */
public class Utility {

    /**
     * Method for converting boolean TRUE and FALSE into integer 1 and 0
     * @param bool value to convert
     * @return converted value
     */
    public static int convertBooleanToInt(boolean bool){
        return bool ? 1 : 0;
    }

    /**
     * Method for converting integer 1 and 0 into boolean TRUE and FALSE
     * @param i value to convert
     * @return converted value
     */
    public static boolean convertIntToBoolean(int i){
        return i == 1;
    }

    /**
     * Method for convert timestamp into:
     * For today: "Today, July 21"
     * For tomorrow:  "Tomorrow"
     * For the next 5 days: "Wednesday" (just the day name)
     * For all days after that: "July 27"
     * @param context
     * @param dateInMillis
     * @return
     */
    public static String getFriendlyDayString(Context context, long dateInMillis) {
        Time time = new Time();
        time.setToNow();
        long currentTime = System.currentTimeMillis();
        int julianDay = Time.getJulianDay(dateInMillis, time.gmtoff);
        int currentJulianDay = Time.getJulianDay(currentTime, time.gmtoff);

        if (julianDay == currentJulianDay) {
            return new SimpleDateFormat("HH:mm").format(dateInMillis);
        } else if ( julianDay < currentJulianDay + 7 ) {
            // If the input date is less than a week in the future, just return the day name.
            return getDayName(context, dateInMillis);
        } else {
            // Otherwise, use the form "July 27"
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("MMM dd");
            return shortenedDateFormat.format(dateInMillis);
        }
    }

    /**
     * Given a day, returns just the name to use for that day.
     * E.g "today", "tomorrow", "wednesday".
     *
     * @param context Context to use for resource localization
     * @param dateInMillis The date in milliseconds
     * @return
     */
    public static String getDayName(Context context, long dateInMillis) {
        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return context.getString(R.string.today);
        } else if ( julianDay == currentJulianDay +1 ) {
            return context.getString(R.string.tomorrow);
        } else {
            Time time = new Time();
            time.setToNow();
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE");
            return dayFormat.format(dateInMillis);
        }
    }
}
