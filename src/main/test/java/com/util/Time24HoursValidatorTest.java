package com.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Time24HoursValidatorTest {

    @Autowired
    private  Time24HoursValidator validator;

    @Test
    public void validate() {

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