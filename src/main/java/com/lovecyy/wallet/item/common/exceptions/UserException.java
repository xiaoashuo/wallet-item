package com.lovecyy.wallet.item.common.exceptions;

import com.lovecyy.wallet.item.common.constants.StatusCode;

/**
 * 转账异常
 *
 * @author linapex
 */
public class UserException extends BaseException {
    private static final long serialVersionUID = 1L;

    public UserException(Throwable ex) {
        super(ex);
    }

    public UserException(String message) {
        super(message);
    }

    public UserException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public int getCode() {
        return StatusCode.UserException;
    }

}
