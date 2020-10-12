package com.lovecyy.wallet.item.netty.core;


import com.lovecyy.wallet.item.netty.handle.init.WebSocketInitializer;
import io.netty.channel.ChannelInitializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * web socket配置
 * @author Yakir
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnProperty(prefix = "netty.websocket",name = "port")
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public WebSocketInitializer webSocketInitializer(WebSocketProperties webSocketProperties){
        return new WebSocketInitializer(webSocketProperties);
    }

    @Bean
    @ConditionalOnBean(value = {ChannelInitializer.class})
    public WebSocketServer webSocketServer(WebSocketProperties webSocketProperties, ChannelInitializer channelInitializer){
      return   new WebSocketServer(webSocketProperties,channelInitializer);
    }


}
