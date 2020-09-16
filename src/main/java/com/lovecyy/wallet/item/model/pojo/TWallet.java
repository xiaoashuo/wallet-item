package com.lovecyy.wallet.item.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
    * 钱包表
    */
@ApiModel(value="com-lovecyy-wallet-item-model-pojo-TWallet")
@Data
@Builder
@TableName(value = "t_wallet")
@NoArgsConstructor
@AllArgsConstructor
public class TWallet implements Serializable {
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
     * 钱包名
     */
    @TableField(value = "name")
    @ApiModelProperty(value="钱包名")
    private String name;

    /**
     * 地址
     */
    @TableField(value = "address")
    @ApiModelProperty(value="地址")
    private String address;

    /**
     * 密码
     */
    @TableField(value = "password")
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 私钥
     */
    @TableField(value = "private_key")
    @ApiModelProperty(value="私钥")
    private String privateKey;

    /**
     * key_store
     */
    @TableField(value = "key_store")
    @ApiModelProperty(value="key_store")
    private String keyStore;

    /**
     * 助记词
     */
    @TableField(value = "mnemonic")
    @ApiModelProperty(value="助记词")
    private String mnemonic;

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

    public static final String COL_NAME = "name";

    public static final String COL_ADDRESS = "address";

    public static final String COL_PASSWORD = "password";

    public static final String COL_PRIVATE_KEY = "private_key";

    public static final String COL_KEY_STORE = "key_store";

    public static final String COL_MNEMONIC = "mnemonic";

    public static final String COL_GMT_CREATE = "gmt_create";

    public static final String COL_GMT_MODIFIED = "gmt_modified";
}