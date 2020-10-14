package com.lovecyy.wallet.item.netty.handle;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovecyy.wallet.item.netty.handle.pojo.Message;
import com.lovecyy.wallet.item.netty.handle.state.UserChannelMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 聊天处理
 * @author Yakir
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static final Logger log= LoggerFactory.getLogger(ChatHandler.class);

    /**
     * 用于记录和管理所有客户端的channels
     * add时已经把管道删除监听注册进去了
     * 所以不用手动删除 调用channel 的close 就会通知监听,调用删除当前管道方法
     */
    private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);



    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        String content = textWebSocketFrame.text();
        System.out.println("接收到的数据: " + content);

        ObjectMapper objectMapper = new ObjectMapper();
        Message message =objectMapper.readValue(content, Message.class);

        switch (message.getType()) {
            //处理客户端链接的消息
            case 0:
                //表示连接
                //建立用户和通道之间的关系
                UserChannelMap.put(message.getUserId(), channelHandlerContext.channel());
                log.info("用户[{}]与[{}]建立了关联",message.getUserId(),  channelHandlerContext.channel().id());
                UserChannelMap.print();
                break;
            case 1:

                Channel channel = UserChannelMap.get(message.getUserId());
                if (channel!=null){
                    channel.writeAndFlush(new TextWebSocketFrame(""));
                }
                break;
            case 2:


                break;
            case 3:
                //检测心跳
                //接收心跳信息
                break;

        }
        /*//将接收的消息发送所有的客户端
        for (Channel channel : channels) {
            channel.writeAndFlush(new TextWebSocketFrame(sdf.format(new Date()) + ":" + content));
        }*/
    }

    //当有新的客户端连接服务器之后,就会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        channels.add(ctx.channel());
    }

    //出现异常是关闭通道
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //根据通道id取消用户和通道的关系
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
        System.out.println("出现异常.....关闭通道!");
    }

    //当客户端关闭链接时关闭通道
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        System.out.println("关闭通道");
        UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
        UserChannelMap.print();
    }

}
