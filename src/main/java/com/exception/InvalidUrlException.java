package com.exception;

public class InvalidUrlException extends Exception {
    public InvalidUrlException(String invalidUrl) {
        super(invalidUrl);
    }
}
