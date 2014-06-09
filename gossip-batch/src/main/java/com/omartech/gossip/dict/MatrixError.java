package com.omartech.gossip.dict;


public class MatrixError extends RuntimeException {

    private static final long serialVersionUID = -8961386981267748942L;

    public MatrixError(final String message) {
        super(message);
    }

    public MatrixError(final Throwable t) {
        super(t);
    }

}
