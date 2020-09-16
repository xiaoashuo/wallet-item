package com.lovecyy.wallet.item.common.exceptions;

import com.lovecyy.wallet.item.common.constants.StatusCode;

/**
 * 转账异常
 *
 * @author linapex
 */
public class TokenException extends BaseException {
    private static final long serialVersionUID = 1L;

    public TokenException(Throwable ex) {
        super(ex);
    }

    public TokenException(String message) {
        super(message);
    }

    public TokenException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public int getCode() {
        return StatusCode.TokenException;
    }

}
