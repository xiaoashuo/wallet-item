package com.lovecyy.wallet.item.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

/**
 * 合约信息
 * @author Yakir
 */
@Data
@NoArgsConstructor
public class ContractInfo {
    public ContractInfo(String address, String name, String symbol, BigInteger decimals, BigInteger totalSupply) {
        this.address = address;
        this.name = name;
        this.symbol = symbol;
        this.decimals = decimals;
        this.totalSupply = totalSupply.divide(BigInteger.TEN.pow(decimals.intValue()));
    }

    /**
     * 合约地址
     */
    private String address;

    /**
     * 合约名称
     */
    private String name;
    /**
     * 合约代表
     */
    private String symbol;

    /**
     * 合约精度
     */
    private BigInteger decimals;
    /**
     * 合约总供应量
     */
    private BigInteger totalSupply;

}
