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
import lombok.Builder;
import lombok.Data;

/**
 * 交易记录表
 */
@ApiModel(value = "com-lovecyy-wallet-item-model-pojo-TTransaction")
@Data
@Builder
@TableName(value = "t_transaction")
public class TTransaction implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private Integer id;

    /**
     * 发起地址
     */
    @TableField(value = "from_address")
    @ApiModelProperty(value = "发起地址")
    private String fromAddress;

    /**
     * 接收地址
     */
    @TableField(value = "to_address")
    @ApiModelProperty(value = "接收地址")
    private String toAddress;

    /**
     * 交易数量
     */
    @TableField(value = "amount")
    @ApiModelProperty(value = "交易数量")
    private BigDecimal amount;

    /**
     * gas单价
     */
    @TableField(value = "gas_price")
    @ApiModelProperty(value = "gas单价")
    private BigInteger gasPrice;

    /**
     * 用量
     */
    @TableField(value = "gas_use")
    @ApiModelProperty(value = "用量")
    private BigInteger gasUse;


    /**
     * 交易hash
     */
    @TableField(value = "trading_hash")
    @ApiModelProperty(value = "交易hash")
    private String tradingHash;

    /**
     * 区块号
     */
    @TableField(value = "block_number")
    @ApiModelProperty(value = "区块号")
    private BigInteger blockNumber;

    /**
     * 交易类型1 普通交易 2 代币交易
     */
    @TableField(value = "type")
    @ApiModelProperty(value = "交易类型1 普通交易 2 代币交易")
    private Integer type;

    /**
     * 合约地址
     */
    @TableField(value = "contract_address")
    @ApiModelProperty(value = "合约地址")
    private String contractAddress;

    /**
     * 交易状态1 成功 0失败
     */
    @TableField(value = "status")
    @ApiModelProperty(value = "交易状态1 成功 0失败")
    private Integer status;


    /**
     * 创建时间
     */
    @TableField(value = "gmt_create")
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreate;

    /**
     * 更新时间
     */
    @TableField(value = "gmt_modified")
    @ApiModelProperty(value = "更新时间")
    private Date gmtModified;

    private static final long serialVersionUID = 1L;

    public static final String COL_ID = "id";

    public static final String COL_FROM_ADDRESS = "from_address";

    public static final String COL_TO_ADDRESS = "to_address";

    public static final String COL_AMOUNT = "amount";

    public static final String COL_GAS_PRICE = "gas_price";

    public static final String COL_GAS_USE = "gas_use";

    public static final String COL_GAS_LIMIT = "gas_limit";

    public static final String COL_TRADING_HASH = "trading_hash";

    public static final String COL_BLOCK_NUMBER = "block_number";

    public static final String COL_TYPE = "type";

    public static final String COL_CONTRACT_ADDRESS = "contract_address";

    public static final String COL_STATUS = "status";

    public static final String COL_REMARK = "remark";

    public static final String COL_TX_TIME = "tx_time";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";

    public static TTransactionBuilder builder() {
        return new TTransactionBuilder();
    }
}