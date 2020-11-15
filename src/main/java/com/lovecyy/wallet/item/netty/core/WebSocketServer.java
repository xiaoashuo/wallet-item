package com.lovecyy.wallet.item.netty.core;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
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
        log.info("开始启动netty websocket");
        server = new ServerBootstrap();
        int bossThreads = webSocketProperties.getBossThreads();
        int workerThreads = webSocketProperties.getWorkerThreads();
        boolean epoll = webSocketProperties.isEpoll();
        if (epoll){
            bossGroup = new EpollEventLoopGroup(bossThreads,
                    new DefaultThreadFactory("WebSocketBossGroup", true));
            workerGroup = new EpollEventLoopGroup(workerThreads,
                    new DefaultThreadFactory("WebSocketWorkerGroup", true));
            server.channel(EpollServerSocketChannel.class);
        }else {
            bossGroup = new NioEventLoopGroup(bossThreads);
            workerGroup = new NioEventLoopGroup(workerThreads);
            server.channel(NioServerSocketChannel.class);
        }
        server.group(bossGroup,workerGroup)
                .childOption(ChannelOption.TCP_NODELAY, true)
//                .option(ChannelOption.SO_BACKLOG, 128) // tcp最大缓存链接个数
//                .childOption(ChannelOption.SO_KEEPALIVE, true) //保持连接
            //    .childOption(ChannelOption.TCP_NODELAY, true)
//                .handler(new LoggingHandler(LogLevel.INFO)) // 打印日志级别
                .childHandler(channelInitializer);
        //启动websocket服务 监听端口
        future = server.bind(webSocketProperties.getPort());
        future.addListener(futureElement ->{
            if (futureElement.isSuccess()){
                log.info("Netty server - 启动成功 绑定端口[{}]",webSocketProperties.getPort());
            }else{
                throw new RuntimeException("Netty server 启动失败");
            }
        } );

    }

}
