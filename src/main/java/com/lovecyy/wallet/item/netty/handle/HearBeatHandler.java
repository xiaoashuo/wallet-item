package com.lovecyy.wallet.item.netty.handle;

import cn.hutool.json.JSONUtil;
import com.lovecyy.wallet.item.common.enums.ResultCodes;
import com.lovecyy.wallet.item.common.utils.ServletUtils;
import com.lovecyy.wallet.item.model.dto.R;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

/**
 * 心跳机制
 * @author Yakir
 */
public class HearBeatHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log= LoggerFactory.getLogger(HearBeatHandler.class);

    /**
     * 触发用户事件
     * @param ctx
     * @param evt
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        //握手完成认证token
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshakeComplete= (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String urlSuffix = handshakeComplete.requestUri();
            String token = ServletUtils.extractUrlParams(urlSuffix);
            if (StringUtils.isEmpty(token)){
                ctx.channel().writeAndFlush(new TextWebSocketFrame(JSONUtil.toJsonStr(R.fail(ResultCodes.TOKEN_NOT_EXISTS))))
                        .addListener(ChannelFutureListener.CLOSE);
            }
        }


        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                //检测到读空闲不做任何的操作
                log.info("读空闲事件触发...");
            }
            else if (idleStateEvent.state() == IdleState.WRITER_IDLE) {
                //检测到写空闲不做任何的操作
                log.info("写空闲事件触发...");
            } else if (idleStateEvent.state() == IdleState.ALL_IDLE) {
                log.info("--------------");
                log.info("读写空闲事件触发,关闭通道资源...");
                ctx.channel().close();
            }
        }
    }
}
