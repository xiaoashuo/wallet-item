package com.lovecyy.wallet.item.netty.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;



/**
 * 解决单列多线程问题
 */
@Slf4j
@ChannelHandler.Sharable
@Component
//@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequiredArgsConstructor
public class WebSocketRateLimitHandler extends ChannelInboundHandlerAdapter {


    //private final RedisService redisService;
    private final int EXPIRE_TIME = 5;
    private final int MAX = 10;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
        String ip = insocket.getAddress().getHostAddress();
        // Generates key
        String key = String.format("ut_wss_limit_rate_%s", ip);
        // Checks
     //   boolean over = redisService.overRequestRateLimit(key, MAX, EXPIRE_TIME, TimeUnit.SECONDS, "websocket");
        boolean over =false;
        if (over) {
            log.debug("IP: {} 触发限流了 ",ip);
            ctx.channel().close();
            return;
        }
        ctx.fireChannelRead(msg);
    }
}
