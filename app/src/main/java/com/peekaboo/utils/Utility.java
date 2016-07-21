package com.peekaboo.utils;

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

}
