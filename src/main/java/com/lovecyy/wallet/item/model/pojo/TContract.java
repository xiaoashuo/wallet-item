package com.lovecyy.wallet.item.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 合约表
    */
@ApiModel(value="com-lovecyy-wallet-item-model-pojo-TContract")
@Data
@Builder
@TableName(value = "t_contract")
@NoArgsConstructor
@AllArgsConstructor
public class TContract implements Serializable {
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
    @TableField(value = "tx_hash")
    @ApiModelProperty(value="合约hash")
    private String txHash;
    /**
     * 合约名称
     */
    @TableField(value = "name")
    @ApiModelProperty(value="合约名称")
    private String name;

    /**
     * 合约标志
     */
    @TableField(value = "symbol")
    @ApiModelProperty(value="合约标志")
    private String symbol;

    /**
     * 合约精度
     */
    @TableField(value = "decimals")
    @ApiModelProperty(value="合约精度")
    private BigInteger decimals;

    /**
     * 总发行量
     */
    @TableField(value = "total_supply")
    @ApiModelProperty(value="总发行量")
    private BigInteger totalSupply;

    @TableField(value = "status")
    @ApiModelProperty(value="合约状态")
    private Integer status;
    /**
     * 合约地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value="合约地址")
    private String address;

    @TableField(value = "issue_address")
    @ApiModelProperty(value="发行合约地址")
    private String issueAddress;
    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value="创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    @ApiModelProperty(value="更新时间")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_UID = "uid";
    public static final String COL_TX_HASH = "tx_hash";

    public static final String COL_NAME = "name";

    public static final String COL_SYMBOL = "symbol";

    public static final String COL_DECIMALS = "decimals";

    public static final String COL_TOTAL_SUPPLY = "total_supply";

    public static final String COL_ADDRESS = "address";
    public static final String COL_STATUS = "status";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}