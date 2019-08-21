package com.exception;

public class InvalidDataFormatException extends Exception {
    public InvalidDataFormatException(String invalidDataFormat) {
        super(invalidDataFormat);
    }
}
