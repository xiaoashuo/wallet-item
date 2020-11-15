package com.lovecyy.wallet.item.netty.handle.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 消息数据
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
     * 用户data
     */
    private Object  data;

}
