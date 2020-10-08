package com.lovecyy.wallet.item.model.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
    * 钱包表
    */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TWalletDto implements Serializable {


    private Integer id;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gmtCreate;



}