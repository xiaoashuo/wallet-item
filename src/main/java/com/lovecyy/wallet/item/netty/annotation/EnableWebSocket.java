package com.lovecyy.wallet.item.netty.annotation;

import com.lovecyy.wallet.item.netty.WebSocketServer;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 启用websocket
 * @author Yakir
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ WebSocketServer.class })
public @interface EnableWebSocket {
}
