package com.exception;

public class InvalidURLException extends Exception {
    public InvalidURLException(String invalid_url) {
        super(invalid_url);
    }
}
