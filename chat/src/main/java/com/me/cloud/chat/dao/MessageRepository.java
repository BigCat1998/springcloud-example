package com.me.cloud.chat.dao;

import com.me.cloud.entity.Message;

import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    int updateByReceiveId(Integer receiveId);

    List<Message> findByMessageList(Integer sendId, Integer receiveId, Integer isReceive, String createDate, Integer page, Integer total);

}
