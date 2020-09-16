package com.lovecyy.wallet.item.common.exceptions;

import com.lovecyy.wallet.item.common.constants.StatusCode;

/**
 * 转账异常
 *
 * @author linapex
 */
public class ContractException extends BaseException {
    private static final long serialVersionUID = 1L;

    public ContractException(Throwable ex) {
        super(ex);
    }

    public ContractException(String message) {
        super(message);
    }

    public ContractException(String message, Throwable ex) {
        super(message, ex);
    }

    @Override
    public int getCode() {
        return StatusCode.TransferException;
    }

}
