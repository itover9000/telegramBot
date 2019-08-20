package com.exception;

public class NoDataOnSiteException extends Exception {
    public NoDataOnSiteException(String noData) {
        super(noData);
    }
}
