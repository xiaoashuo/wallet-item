package com.lovecyy.wallet.item.netty.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * FullHttpRequest
 */

@ChannelHandler.Sharable
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            HttpHeaders headers = request.headers();
            if (headers.size() < 1) {
                ctx.channel().close();
                return;
            }
            String token = headers.get("token");
            //Claims claims = jwtOperator.getClaimsFromToken(token);
            //Long uid = Long.valueOf(claims.get("uid") + "");
           // userChannelManager.add(uid, ctx.channel());
       //     log.debug("Authentication success. uid: " + uid);
            ctx.pipeline().remove(this);
            ctx.fireChannelRead(msg);
        } else {
            ctx.channel().close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //userChannelManager.remove(ctx.channel());
        ctx.channel().close();
    }
}
