package com.lovecyy.wallet.item.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 用户关联合约地址表
    */
@ApiModel(value="com-lovecyy-wallet-item-model-pojo-TUserRelationContract")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName(value = "t_user_relation_contract")
public class TUserRelationContract implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id")
    private Integer id;

    /**
     * 用户id
     */
    @TableField(value = "uid")
    @ApiModelProperty(value="用户id")
    private Integer uid;

    /**
     * 用户钱包地址
     */
    @TableField(value = "wallet_address")
    @ApiModelProperty(value="用户钱包地址")
    private String walletAddress;

    /**
     * 合约地址
     */
    @TableField(value = "contract_address")
    @ApiModelProperty(value="合约地址")
    private String contractAddress;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_UID = "uid";

    public static final String COL_WALLET_ADDRESS = "wallet_address";

    public static final String COL_CONTRACT_ADDRESS = "contract_address";
}