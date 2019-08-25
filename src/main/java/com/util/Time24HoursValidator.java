package com.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Time24HoursValidator {
    private static final String TIME_24_HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    /*
    (# start group 1
     # 0?[1-9]              01-09 or 1-9
     # or [12][0-9]        10-19 or 20-29
     # or 3[01]            30 or 31 )
     # end group 1
     #  "."
     # start group 2
     # 0?[1-9]              01-09 or 1-9
     # or 1[012]           10,11 or 12
     # end group 2
*/
    private static final String DATE_PATTERN = "(0?[1-9]|[12][0-9]|3[01])\\.(0?[1-9]|1[012])";
    private final Pattern patternTime24;
    private final Pattern patternDay;

    public Time24HoursValidator() {
        patternTime24 = Pattern.compile(TIME_24_HOURS_PATTERN);
        patternDay = Pattern.compile(DATE_PATTERN);
    }

    public boolean isValidateTime24(String time) {
        return patternTime24.matcher(time).matches();
    }

    public boolean isValidateDate(String date) {
        return patternDay.matcher(date).matches();
    }
}