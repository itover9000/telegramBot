package com.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Time24HoursValidator {
    private static final String TIME_24_HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";


    /*(         
    (          # Нaчaлo гpyппы 1     0?[1-9]         # 01-09 или 1-9
     |         # Или     [12][0-9]       # 10-19 или 20-29
    |         # Или     3[01]           # 30 или 31 )
    # Koнeц гpyппы 1
     .         # Дaльшe дoлжнa быть тoчka "."
    (          # Нaчaлo гpyппы 2     0?[1-9]         # 01-09 или 1-9
    |         # Или     1[012]          # 10,11 или 12 )
    # Koнeц гpyппы 2
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