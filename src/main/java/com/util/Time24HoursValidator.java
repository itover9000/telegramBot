package com.util;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Time24HoursValidator {
    private static final String TIME_24_HOURS_PATTERN = "([01]?[0-9]|2[0-3]):[0-5][0-9]";
    private final Pattern pattern;

    public Time24HoursValidator() {
        pattern = Pattern.compile(TIME_24_HOURS_PATTERN);
    }

    public boolean validate(String time) {
        return pattern.matcher(time).matches();
    }

}
