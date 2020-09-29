package com.lovecyy.wallet.item.common.utils;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.NumberUtil;
import org.springframework.util.Assert;
import org.springframework.util.NumberUtils;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;

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
     * 16进制转为10进制 带前缀
     * @param number 0x123545
     * @return
     */
    public static BigInteger hexToDec(String number){
        return  Numeric.decodeQuantity(number);
    }

    /**
     * 16进制转10进制 无前缀
     * @param number 123545 十六进制的
     * @return
     */
    public static BigInteger hexToDecNoPrefix(String number){
        return new BigInteger(number,16);
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
    public static BigDecimal WeiTOEth(String balance){
        return Convert.fromWei(balance, Convert.Unit.ETHER);
    }
    public static BigDecimal EthTOWei(String balance){
        return Convert.toWei(balance, Convert.Unit.ETHER);
    }


}
