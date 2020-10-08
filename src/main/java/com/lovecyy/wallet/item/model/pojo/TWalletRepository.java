package com.lovecyy.wallet.item.model.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * t_wallet_repository
 * @author 
 */
@TableName(value = "t_wallet_repository")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TWalletRepository implements Serializable {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 钱包名
     */
    @TableField(value = "name")
    private String name;

    /**
     * 地址
     */
    @TableField(value = "address")
    private String address;

    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;

    /**
     * 私钥
     */
    @TableField(value = "private_key")
    private String privateKey;

    /**
     * key_store
     */
    @TableField(value = "key_store")
    private String keyStore;

    /**
     * 助记词
     */
    @TableField(value = "mnemonic")
    private String mnemonic;

    /**
     * 是否可以 1 可用 0 不可用
     */
    @TableField(value = "used")
    private Integer used;

    private static final long serialVersionUID = 1L;
}