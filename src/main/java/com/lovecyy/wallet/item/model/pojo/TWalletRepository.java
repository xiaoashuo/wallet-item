package com.lovecyy.wallet.item.model.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * t_wallet_repository
 * @author 
 */
@Data
public class TWalletRepository implements Serializable {
    /**
     * id
     */
    private Integer id;

    /**
     * 钱包名
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 密码
     */
    private String password;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * key_store
     */
    private String keyStore;

    /**
     * 助记词
     */
    private String mnemonic;

    /**
     * 是否可以 1 可用 0 不可用
     */
    private Integer used;

    private static final long serialVersionUID = 1L;
}