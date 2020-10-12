package com.lovecyy.wallet.item.netty.handle.pojo;

/**
 * 消息数据
 */
public class Message {

    private Integer type;//消息类型

    private ChatRecord chatRecord;//聊天消息

    private Object ext; //附加消息

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public ChatRecord getChatRecord() {
        return chatRecord;
    }

    public void setChatRecord(ChatRecord chatRecord) {
        this.chatRecord = chatRecord;
    }

    public Object getExt() {
        return ext;
    }

    public void setExt(Object ext) {
        this.ext = ext;
    }
}
