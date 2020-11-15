package com.lovecyy.wallet.item.netty.handle.init;

import com.lovecyy.wallet.item.common.utils.SpringUtils;
import com.lovecyy.wallet.item.netty.core.WebSocketProperties;
import com.lovecyy.wallet.item.netty.handle.AuthHandler;
import com.lovecyy.wallet.item.netty.handle.ChatHandler;
import com.lovecyy.wallet.item.netty.handle.HearBeatHandler;
import com.lovecyy.wallet.item.netty.handle.WebSocketRateLimitHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * websocket初始化器
 * @author Yakir
 */
public class WebSocketInitializer extends ChannelInitializer<SocketChannel> {

    private  WebSocketProperties webSocketProperties;
    private  WebSocketRateLimitHandler webSocketRateLimitHandler;
    private  AuthHandler authHandler;

    public WebSocketInitializer(WebSocketProperties webSocketProperties) {
        this.webSocketProperties=webSocketProperties;
        webSocketRateLimitHandler= SpringUtils.getBean("webSocketRateLimitHandler");
        authHandler= SpringUtils.getBean("authHandler");
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        //-------------
        //用于支持 Http协议
        //-----------------

        //websocket基于 http协议,需要有 http 的编解码器
        pipeline.addLast(webSocketRateLimitHandler)
                .addLast(new HttpServerCodec())
                //对于大数据流的支持
                .addLast(new ChunkedWriteHandler())
                //添加对HTTP请求和响应的聚合器:只要使用Netty进行 http编码都需要使用到
                //对HttpMessage进行聚合,聚合成FullHttpRequest或者FullHttpResponse
                //在 netty 编程总都会使用到Handler
                .addLast(new HttpObjectAggregator(1024*64))
                // ====================== 以下是支持httpWebsocket ======================
               // .addLast(authHandler)
                /**
                 * websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
                 * 本handler会帮你处理一些繁重的复杂的事
                 * 会帮你处理握手动作： handshaking（close, ping, pong） ping + pong = 心跳
                 * 对于websocket来讲，都是以frames进行传输的，不同的数据类型对应的frames也不同
                 */
                .addLast(new WebSocketServerProtocolHandler(webSocketProperties.getContextPath(),webSocketProperties.getCheckStartsWith()))
                // ====================== 增加心跳支持 start    ======================
                //添加Netty空闲超时检查的支持
                //4:读空闲超时,8:写空闲超时,12: 读写空闲超时
                .addLast(new IdleStateHandler(webSocketProperties.getReaderIdleTimeSeconds(),webSocketProperties.getWriterIdleTimeSeconds(),webSocketProperties.getAllIdleTimeSeconds()))
                .addLast(new HearBeatHandler())
                // ====================== 增加心跳支持 end    ======================
                //添加自定有的 handler
                .addLast(new ChatHandler());




    }
}
