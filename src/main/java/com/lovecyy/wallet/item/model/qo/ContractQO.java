package com.lovecyy.wallet.item.model.qo;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigInteger;

/**
 * 合约QO
 */
@Data
public class ContractQO {
    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 钱包地址
     */
    @Length(min = 40,max = 42,message = "钱包地址不正确")
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
