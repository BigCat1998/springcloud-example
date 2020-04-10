package com.me.cloud.chat.dao;

import com.me.cloud.entity.MessageList;

import java.util.List;

public interface MessageListRepository {

    MessageList save(MessageList messageList);

    int findBySendIdAndReceiveId(Integer sendId, Integer receiveId);

    List<MessageList> findByReceiveId(Integer receiveId);

}
