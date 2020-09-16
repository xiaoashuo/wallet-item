package com.lovecyy.wallet.item.common.exceptions;

/**
 * BaseException
 *
 * @author linapex
 */
public abstract class BaseException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public BaseException(Throwable ex) {
        super(ex);
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable ex) {
        super(message, ex);
    }

    public abstract int getCode();
}
