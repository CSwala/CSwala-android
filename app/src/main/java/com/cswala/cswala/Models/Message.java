package com.cswala.cswala.Models;


public class Message {
    private String msgId;
    private String senderId;
    private String senderName;
    private String content;
    private long createdTime;
    private String imageUrl;

    public Message() {
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getMsgId() {
        return msgId;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getContent() {
        return content;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public String getImageUrl() { return imageUrl; }

    public Message(String msgId, String senderName, String senderId, String content, long createdTime, String imageUrl) {
        this.msgId = msgId;
        this.senderName = senderName;
        this.senderId = senderId;
        this.content = content;
        this.createdTime = createdTime;
        this.imageUrl = imageUrl;
    }
}
