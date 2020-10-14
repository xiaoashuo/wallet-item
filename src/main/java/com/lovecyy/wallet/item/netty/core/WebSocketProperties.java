package com.lovecyy.wallet.item.netty.core;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * web socket自动配置属性
 * @author Yakir
 */
@ConfigurationProperties("netty.websocket")
@Data
public class WebSocketProperties {
    /**
     * 监听端口
     */
    private Integer port=9090;
    /**
     * url路径前缀
     */
    private String prefix="/default";
    /**
     * 检查开始 false 全匹配prefix 否在以开头即可
     * 匹配通过则升级协议
     */
    private Boolean checkStartsWith=false;
    /**
     * 读空闲时间
     */
    private Integer readerIdleTimeSeconds=4;
    /**
     * 写空闲时间
     */
    private Integer writerIdleTimeSeconds=8;
    /**
     * 读写空闲时间
     */
    private Integer allIdleTimeSeconds=12;

}
