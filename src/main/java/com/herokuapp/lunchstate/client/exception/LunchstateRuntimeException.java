package com.herokuapp.lunchstate.client.exception;

public class LunchstateRuntimeException extends RuntimeException {

    public LunchstateRuntimeException() {
        super();
    }

    public LunchstateRuntimeException(String s, Throwable cause) {
        super(s, cause);
    }

    public LunchstateRuntimeException(String s) {
        super(s);
    }

    public LunchstateRuntimeException(Throwable cause) {
        super(cause);
    }

}
