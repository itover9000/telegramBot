package com.exception;

public class NoDataOnTheSiteException extends Exception {
    public NoDataOnTheSiteException(String noData) {
        super(noData);
    }
}
