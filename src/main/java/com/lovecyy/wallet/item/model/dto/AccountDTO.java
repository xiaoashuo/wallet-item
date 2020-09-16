package com.lovecyy.wallet.item.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {
    /**
     * 钱包地址
     */
    private String address;
    /**
     * 钱包余额
     */
    private BigDecimal balance;
}
