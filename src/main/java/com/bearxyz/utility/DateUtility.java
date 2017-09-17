package com.bearxyz.utility;

import java.sql.Date;
import java.util.Calendar;

/**
 * Created by bearxyz on 2017/9/15.
 */
public class DateUtility {

    public static int daysOfTwo(Date fDate, Date oDate) {
        Calendar aCalendar = Calendar.getInstance();
        aCalendar.setTime(fDate);
        int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
        aCalendar.setTime(oDate);
        int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
        return day2 - day1;
    }

}
