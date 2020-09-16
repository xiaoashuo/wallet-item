package com.lovecyy.wallet.item.common.utils;

import org.springframework.util.Assert;
import org.web3j.utils.Convert;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FormatConvert {
    /**
     * 得到去除精度的值
     * @param number
     * @param decimals
     * @return
     */
    public static BigInteger getNumberRemoveDecimal(BigInteger number,BigInteger decimals){
        return number.divide(BigInteger.TEN.pow(decimals.intValue()));
    }

    /**
     * 设置值 加上精度
     * @param number
     * @param decimals
     * @return
     */
    public static BigInteger setNumberAddDecimal(BigDecimal number, BigInteger decimals){
        return  number.multiply(BigDecimal.TEN.pow(decimals.intValue())).toBigInteger();
    }

    /**
     * 从v到gv
     * @param number
     * @return
     */
    public static BigInteger WeiTOGWei(BigInteger number){
        return Convert.fromWei(number.toString(), Convert.Unit.GWEI).toBigInteger();
    }
    /**
     * GWEI转换为WEI
     * @param number
     * @return
     */
    public static BigInteger GWeiTOWei(BigDecimal number){
        Assert.notNull(number,"数量不能为空");
        return Convert.toWei(number, Convert.Unit.GWEI).toBigInteger();
    }

    /**
     * ETH转wei
     * @param balance
     * @return
     */
    public static BigDecimal EthTOWei(BigDecimal balance){
        return Convert.fromWei(balance, Convert.Unit.ETHER);
    }

    /**
     * ETH 转wei
     * @param balance
     * @return
     */
    public static BigDecimal EthTOWei(String balance){
        return Convert.fromWei(balance, Convert.Unit.ETHER);
    }
}
