package com.util;

import org.junit.Test;

import static org.junit.Assert.*;

public class Time24HoursValidatorTest {

    @Test
    public void validate() {
        Time24HoursValidator validator = new Time24HoursValidator();

        assertTrue(validator.validate("00:00"));
        assertTrue(validator.validate("0:00"));
        assertTrue(validator.validate("23:59"));

        assertFalse(validator.validate("00:0"));
        assertFalse(validator.validate("0:0"));

        assertFalse(validator.validate("25:59"));
        assertFalse(validator.validate("-25:59"));
        assertFalse(validator.validate("23:60"));
        assertFalse(validator.validate("23: 59"));
    }
}