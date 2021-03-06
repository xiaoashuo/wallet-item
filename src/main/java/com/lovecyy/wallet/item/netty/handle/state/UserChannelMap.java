package com.lovecyy.wallet.item.netty.handle.state;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户id与管道的关系
 * @author Yakir
 */
public class UserChannelMap {

    //保存用户id和通道的map对象
    private static Map<Integer, Channel> userChannelMap;

    static {
        userChannelMap = new HashMap<>();
    }

    /**
     * 建立用户和通道直接的关联
     *
     * @param userid
     * @param channel
     */
    public static void put(Integer userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    /**
     * 解除用户和通道直接的关系
     *
     * @param userid
     */
    public static void remove(Integer userid) {
        userChannelMap.remove(userid);
    }

    public static void removeByChannelId(String channelId) {
        for (Integer userId : userChannelMap.keySet()) {
            if (userChannelMap.get(userId).id().asLongText().equals(channelId)) {
                System.out.println("客户端连接断开,取消用户" + userId + "与通道" + channelId + "的关联");
                userChannelMap.remove(userId);
                break;
            }

        }
    }

    // 打印所有的用户与通道的关联数据
    public static void print() {
        for (Integer s : userChannelMap.keySet()) {
            System.out.println("用户id:" + s + " 通道:" + userChannelMap.get(s).id());
        }
    }

    /**
     * 根据用户id,获取通道
     */
    public static Channel get(Integer userid) {
        return userChannelMap.get(userid);
    }
}
