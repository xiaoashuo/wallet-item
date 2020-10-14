package com.lovecyy.wallet.item.netty.handle.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * 消息数据
 */
@Data
public class Message implements Serializable {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 用户token
     */
    private String token;

}
