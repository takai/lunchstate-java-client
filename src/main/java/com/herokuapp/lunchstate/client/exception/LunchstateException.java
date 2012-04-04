package com.herokuapp.lunchstate.client.exception;

public class LunchstateException extends Exception {

    public LunchstateException() {
        super();
    }

    public LunchstateException(String s, Throwable cause) {
        super(s, cause);
    }

    public LunchstateException(String s) {
        super(s);
    }

    public LunchstateException(Throwable cause) {
        super(cause);
    }

}
