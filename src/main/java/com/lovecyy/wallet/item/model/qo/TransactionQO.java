package com.lovecyy.wallet.item.model.qo;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

@Data
public class TransactionQO implements Serializable {

    /**当前交易类型 1.普通交易 2代币交易 3 部署合约*/
    private Integer transactionType;
    /**当前查询操作类型 全部0 转出1 转入2 失败3*/
    private Integer optionType;
    /**当前用户钱包地址*/
    @NotEmpty(message = "钱包地址不能为空")
    private String address;
    /**合约地址*/
    private String contractAddress;
    /**状态 0失败 1成功*/
    private Integer status;
    /**起始页*/
    private Integer pageNum;
    /**页大小*/
    private Integer pageSize;
}
