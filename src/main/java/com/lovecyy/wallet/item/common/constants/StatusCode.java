package com.lovecyy.wallet.item.common.constants;

/**
 * 状态码
 * @author Yakir
 */
public class StatusCode {
    public static final int GENERAL_ERRORCODE = -99; //通用异常

    public static final int TransferException = -113; //-113，转账异常
    public static final int ContractException = -114; //-113，合约异常
    public static final int UserException = -115; //-113，合约异常
    public static final int TokenException = -116; //-113，合约异常
    public static final int LOGIN_TOKEN_EXCEPTION = 999; //请求头错误未携带token
    public static final int LOGIN_TOKEN_EXPIRE = 1001; //token已过期


}

