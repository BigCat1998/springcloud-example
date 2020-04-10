package com.me.cloud.chat.model;

import com.me.cloud.entity.Message;
import com.me.cloud.entity.User;
import lombok.Data;

import java.util.List;


@Data
public class MessageListVo {

    private User user;

    private List<UserMessageList> userMessageLists;

    @Data
    public static class UserMessageList{

        private User user;

        private List<Message> messages;
    }
}

