package com.lovecyy.wallet.item.netty.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PreDestroy;

/**
 * websocket 初始化
 * @author Yakir
 */
public class WebSocketServer {

    private static final Logger log= LoggerFactory.getLogger(WebSocketServer.class);

    /**
     * 主线程
     */
    private EventLoopGroup bossGroup;
    /**
     * 工作线程
     */
    private EventLoopGroup workerGroup;
    /**
     * 服务器
     */
    private ServerBootstrap server;
    /**
     * 回调
     */
    private ChannelFuture future;


    @PreDestroy
    public void destroy(){
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        future.channel().close();
        log.info("netty server future通道关闭");
    }

    public WebSocketServer(WebSocketProperties webSocketProperties, ChannelInitializer channelInitializer) {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        server = new ServerBootstrap();
        server.group(bossGroup,workerGroup)
//                .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
//                .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
//                .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
                .channel(NioServerSocketChannel.class)
                .childHandler(channelInitializer);
        //启动websocket服务 监听端口
        future = server.bind(webSocketProperties.getPort());
        log.info("netty server - 启动成功");

    }

}
