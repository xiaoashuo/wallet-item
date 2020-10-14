package com.lovecyy.wallet.item.common.enums;

import lombok.Getter;

@Getter
public enum ResultCodes {
    TOKEN_EXPIRE(999,"token expire"),
    TOKEN_NOT_EXISTS(1001,"token not exists");
    Integer code;
    String msg;

    ResultCodes(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
