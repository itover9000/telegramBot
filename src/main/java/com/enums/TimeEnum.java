package com.enums;

public enum TimeEnum {
    HOUR(" час "),
    HOURS(" часов "),
    HOURS_OTHER(" часа "),
    MINUTE(" минуту назад"),
    MINUTES(" минут назад"),
    MINUTES_OTHER(" минуты назад");


    private String translation;

    TimeEnum(String translation) {
        this.translation = translation;
    }

    public String getTranslation() {
        return translation;
    }
}
