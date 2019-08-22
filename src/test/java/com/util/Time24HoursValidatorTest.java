package com.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Time24HoursValidatorTest {

    @Autowired
    private Time24HoursValidator validator;

    @Test
    public void isValidateTime24() {

        assertTrue(validator.isValidateTime24("00:00"));
        assertTrue(validator.isValidateTime24("0:00"));
        assertTrue(validator.isValidateTime24("23:59"));

        assertFalse(validator.isValidateTime24("00:0"));
        assertFalse(validator.isValidateTime24("0:0"));

        assertFalse(validator.isValidateTime24("25:59"));
        assertFalse(validator.isValidateTime24("-25:59"));
        assertFalse(validator.isValidateTime24("23:60"));
        assertFalse(validator.isValidateTime24("23: 59"));

    }


    @Test
    public void isValidateDate() {

        assertTrue(validator.isValidateDate("11.06"));
        assertTrue(validator.isValidateDate("01.01"));
        assertTrue(validator.isValidateDate("31.07"));
        assertTrue(validator.isValidateDate("31.12"));

        assertFalse(validator.isValidateDate("00.03"));
        assertFalse(validator.isValidateDate("-00.03"));
        assertFalse(validator.isValidateDate("-00.03"));
        assertFalse(validator.isValidateDate("300.3"));
        assertFalse(validator.isValidateDate("3.399"));
        assertFalse(validator.isValidateDate("31.13"));
        assertFalse(validator.isValidateDate("11:06"));
    }
}
