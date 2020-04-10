package com.me.cloud.entity;

import lombok.Data;

import java.util.Date;

/**
 * 消息记录表
 */
@Data
public class Message {

    private Integer id;

    /**
     * 发消息用户id
     */
    private Integer sendId;

    /**
     * 接消息用户id
     */
    private Integer receiveId;

    /**
     * 消息内容
     */
    private String messageData;

    /**
     * 发送时间
     */
    private Date createDate;

    /**
     * 是否已经接收
     *  0已接收
     *  1未接收
     */
    private Integer isReceive;

}
