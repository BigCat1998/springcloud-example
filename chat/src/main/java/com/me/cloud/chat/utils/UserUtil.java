package com.me.cloud.chat.utils;

import com.me.cloud.chat.dao.MessageListRepository;
import com.me.cloud.chat.dao.MessageRepository;
import com.me.cloud.chat.dao.UserRepository;
import com.me.cloud.chat.model.MessageListVo;
import com.me.cloud.common.utils.DateUtil;
import com.me.cloud.entity.Message;
import com.me.cloud.entity.MessageList;
import com.me.cloud.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class UserUtil {

    private static UserRepository userRepository = null;

    private static MessageRepository messageRepository = null;

    private static MessageListRepository messageListRepository = null;

    public UserUtil(UserRepository userRepository, MessageRepository messageRepository, MessageListRepository messageListRepository) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.messageListRepository = messageListRepository;
    }

    /**
     * 修改用户消息状态
     *
     * @param receiveId
     */
    public static void updateUserMessage(Integer receiveId){

        if(messageRepository.updateByReceiveId(receiveId) > 0){
            log.info("updateUserMessage -> 修改成功");
            return;
        }
        log.info("updateUserMessage -> 修改失败");
    }

    /**
     * 未查看消息列表
     *
     * @param receiveId
     * @return
     */
    public static MessageListVo getMessageList(Integer receiveId) {

        List<MessageListVo.UserMessageList> userMessageLists = new ArrayList<>();

        // 获取消息列表
        List<MessageList> messageLists = messageListRepository.findByReceiveId(receiveId);

        for (MessageList messageList : messageLists) {
            List<Message> messages = messageRepository.findByMessageList(messageList.getSendId(), receiveId, 1, DateUtil.getCurrentDate(), 0, 10);

            if (messages.size() <= 0) {
                break;
            }

            MessageListVo.UserMessageList userMessageList = new MessageListVo.UserMessageList();
            userMessageList.setUser(findById(messageList.getSendId()));
            userMessageList.setMessages(messages);

            userMessageLists.add(userMessageList);
        }

        MessageListVo messageListVo = new MessageListVo();
        messageListVo.setUserMessageLists(userMessageLists);
        return messageListVo;
    }

    /**
     * 添加用户消息列表
     *
     * @param sendId
     * @param receiveId
     */
    public static void setMessageList(Integer sendId, Integer receiveId) {

        // 当消息列表不存在此用户的时候，添加此用户到消息列表
        if (messageListRepository.findBySendIdAndReceiveId(sendId, receiveId) == 0) {
            MessageList messageList = new MessageList();
            messageList.setSendId(sendId);
            messageList.setReceiveId(receiveId);

            messageListRepository.save(messageList);
        }
    }

    /**
     * 根据用户 id 查询用户信息
     *
     * @param id
     * @return
     */
    public static User findById(Integer id) {
        return userRepository.findUserById(id);
    }

    /**
     * 添加消息记录
     *
     * @param sendId
     * @param receiveId
     * @param messageData
     * @param isReceive
     * @return
     */
    public static Message setMessage(Integer sendId, Integer receiveId, String messageData, Integer isReceive) {
        Message message = new Message();
        message.setSendId(sendId);
        message.setReceiveId(receiveId);
        message.setCreateDate(new Date());
        message.setMessageData(messageData);
        message.setIsReceive(isReceive);

        return messageRepository.save(message);
    }

}
