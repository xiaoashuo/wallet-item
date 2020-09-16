package com.lovecyy.wallet.item.model.qo;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class TokenQO {
    /**
     * 发送方地址
     */
    private String fromAddress;
    /**
     * 接收方地址
     */
    private String toAddress;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * 数量
     */
    private BigDecimal amount;
    /**
     * 天然器单价
     */
    private BigDecimal gasPrice;
}
