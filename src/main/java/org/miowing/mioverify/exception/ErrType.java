package org.miowing.mioverify.exception;

public enum ErrType {
    FORBIDDEN_OP("ForbiddenOperationException"),
    ILLEGAL_ARG("IllegalArgumentException");

    private final String error;
    ErrType(String error) {
        this.error = error;
    }
    public String v() {
        return error;
    }
    @Override
    public String toString() {
        return error;
    }
}