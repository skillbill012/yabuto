package com.dajudge.ymlgen.api.exceptions;

public class YabutoException extends RuntimeException {
    public YabutoException(String s) {
        super(s);
    }

    public YabutoException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
