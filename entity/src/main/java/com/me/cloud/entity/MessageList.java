package com.me.cloud.entity;

import lombok.Data;

@Data
public class MessageList {

    private Integer id;

    /**
     * 发消息用户id
     */
    private Integer sendId;

    /**
     * 接消息用户id
     */
    private Integer receiveId;

}
