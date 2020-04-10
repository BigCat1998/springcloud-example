package com.me.cloud.chat.socket;

import com.alibaba.fastjson.JSONObject;
import com.me.cloud.chat.model.MessageListVo;
import com.me.cloud.chat.utils.UserUtil;
import com.me.cloud.entity.User;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.yeauty.annotation.*;
import org.yeauty.pojo.Session;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author 大猫
 */
@Slf4j
@Component
@ServerEndpoint(host = "${ws.host}", port = "${ws.port}", path = "${ws.path}")
public class WebSocketServer {

    /**
     * 在线数
     */
    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    private User user;
    private Session session;

    /**
     * 当有新的连接进入时，对该方法进行回调 注入参数的类型:Session、HttpHeaders...
     *
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param arg
     * @param pathMap
     */
    @BeforeHandshake
    public void handshake(Session session,
                          HttpHeaders headers,
                          @RequestParam String req,
                          @RequestParam MultiValueMap reqMap,
                          @PathVariable String arg,
                          @PathVariable Map pathMap) {
        session.setSubprotocols("stomp");

        if (req == null) {
            // 当没有传入用户 id，连接失败
            session.close();
            return;
        }

        // 目前测试传入 req 为当前登录用户id
        log.info("handshake -> req:" + req);

        User user = UserUtil.findById(Integer.parseInt(req));

        if (user == null) {
            session.close();
            return;
        }

        this.user = user;
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();
        log.info("handshake -> 上线用户信息 -> " + user.toString());
        log.info("handshake -> 当前在线用户总数 -> " + getOnlineCount());
    }

    /**
     * 当有新的WebSocket连接完成时，对该方法进行回调 注入参数的类型:Session、HttpHeaders...
     *
     * @param session
     * @param headers
     * @param req
     * @param reqMap
     * @param arg
     * @param pathMap
     */
    @OnOpen
    public void onOpen(Session session,
                       HttpHeaders headers,
                       @RequestParam String req,
                       @RequestParam MultiValueMap reqMap,
                       @PathVariable String arg,
                       @PathVariable Map pathMap) {
        // 在线数加1
        addOnlineCount();

        Integer userId = Integer.parseInt(req);
        MessageListVo messageListVo = UserUtil.getMessageList(userId);
        messageListVo.setUser(user);
        session.sendText(messageListVo.toString());

        if(messageListVo.getUserMessageLists().size() > 0){
            // 修改消息状态
            UserUtil.updateUserMessage(userId);
        }
    }

    /**
     * 当有WebSocket连接关闭时，对该方法进行回调 注入参数的类型:Session
     *
     * @param session
     * @throws IOException
     */
    @OnClose
    public void onClose(Session session) throws IOException {

        log.info("onClose -> 一个连接已关闭");
        webSocketSet.remove(this);
        subOnlineCount();
        log.info("onClose -> 当前在线用户总数 -> " + getOnlineCount());
    }

    /**
     * 当有WebSocket抛出异常时，对该方法进行回调 注入参数的类型:Session、Throwable
     *
     * @param session
     * @param throwable
     */
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }

    /**
     * 当接收到字符串消息时，对该方法进行回调 注入参数的类型:Session、String
     *
     * @param session
     * @param message
     */
    @OnMessage
    public void onMessage(Session session, String message) {

        JSONObject messageBody = JSONObject.parseObject(message);

        // 用户在线时
        for (WebSocketServer item : webSocketSet) {
            // 用户在线直接发送信息给用户
            if (item.user.getUserId() == messageBody.getInteger("receiveId")) {
                log.info("onMessage -> 发消息用户Id -> " + user.getUserId());
                log.info("onMessage -> 接消息用户Id -> " + messageBody.getInteger("receiveId"));
                log.info("onMessage -> 消息内容 -> " + messageBody.getString("message"));

                UserUtil.setMessageList(user.getUserId(), messageBody.getInteger("receiveId"));
                UserUtil.setMessage(user.getUserId(), messageBody.getInteger("receiveId"), messageBody.getString("message"), 0);
                item.session.sendText(messageBody.getString("message"));
                return;
            }
        }

        // 用户不在线时
        log.info("onMessage -> 发消息用户Id -> " + user.getUserId());
        log.info("onMessage -> 接消息用户Id -> " + messageBody.getInteger("receiveId"));
        log.info("onMessage -> 消息内容 -> " + messageBody.getString("message"));

        UserUtil.setMessageList(user.getUserId(), messageBody.getInteger("receiveId"));
        UserUtil.setMessage(user.getUserId(), messageBody.getInteger("receiveId"), messageBody.getString("message"), 1);
    }

    /**
     * 当接收到二进制消息时，对该方法进行回调 注入参数的类型:Session、byte[]
     *
     * @param session
     * @param bytes
     */
    @OnBinary
    public void onBinary(Session session, byte[] bytes) {

        for (byte b : bytes) {
            log.info("onBinary - > 二进制消息 -> " + b);
        }
        session.sendBinary(bytes);
    }

    /**
     * 当接收到 Netty 的事件时，对该方法进行回调 注入参数的类型: Session、Object
     * 心跳检测
     *
     * @param session
     * @param evt
     */
    @OnEvent
    public void onEvent(Session session, Object evt) {

        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    log.info("闲读");
                    break;
                case WRITER_IDLE:
                    log.info("写闲置");
                    break;
                case ALL_IDLE:
                    log.info("都闲着");
                    break;
                default:
                    break;
            }
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        if (WebSocketServer.onlineCount > 0) {
            WebSocketServer.onlineCount--;
        }
    }

}