package com.chartroom.service;
import com.chartroom.entity.MessageFromClient;
import com.chartroom.entity.MessageToClient;
import com.chartroom.utils.CommUtils;

import java.util.Arrays;
import java.util.List;
import	java.util.concurrent.ConcurrentHashMap;


import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-10 11:49
 * @Description:
 */
@ServerEndpoint("/websocket")  // 把当前类标记为 Websocket类
public class WebSocket {
    // 存储所有连接到后端的 websocket
    private static CopyOnWriteArraySet<WebSocket> clients =
            new CopyOnWriteArraySet<>();
    // 缓存所用的用户列表
    private static Map<String,String> namespaces = new ConcurrentHashMap<>();

    // 绑定当前websocket会话
    private Session session;

    // 当前客户端的用户名
    private String userName;

    @OnOpen
    // 浏览器与 websocket建立连接
    public void onOpen(Session session) {
        // 绑定当前 session
        this.session = session;
        // username =' + '${username}'
        String userName = session.getQueryString().split("=")[1]; // 字符串拆分
        this.userName = userName;
        // 将客户端聊天实体保存到 clients
        clients.add(this);
        //将当前用户以及SessionID保存到用户列表
        namespaces.put(session.getId(),userName);
        System.out.println("有新的连接，当前SessionID为:"+session.getId()+",用户名为"
                + userName +",当前聊天室共有 "+clients.size()+" 人");

        //发送给所有用户上线通知(QQ好友上线通知)
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName + "上线了！");
        // 更新当前所有用户列表
        messageToClient.setNames(namespaces);
        // 给当前所有用户发送登录信息
        String JSONStr = CommUtils.object2Json(messageToClient);
        for(WebSocket webSocket : clients) {
            webSocket.sendMsg(JSONStr);
        }
    }

    @OnError
    // 浏览器与后端连接失败
    public void onError(Throwable e) {
        System.err.println("WebSocket连接失败！");
        e.printStackTrace();
    }

    @OnMessage
    // 服务器收到了来自浏览器的信息

    /**
     * 群聊:{"msg":"777","type":1}
     * 私聊:{"to":"0-","msg":"33333","type":2}
     */
    public void onMessage(String msg) {
        // 将 msg反序列化为 MessageFromClient
        MessageFromClient messageFromClient = (MessageFromClient) CommUtils.json2Object
                (msg,MessageFromClient.class);

        //根据type的类型来做响应的区分
        if (messageFromClient.getType().equals("1")) {
            // 群聊信息
            String content = messageFromClient.getMsg();
            MessageToClient messageToClient = new MessageToClient();
            messageToClient.setContent(content);
            messageToClient.setNames(namespaces);
            // 广播发送
            for (WebSocket webSocket : clients) {
                webSocket.sendMsg(CommUtils.object2Json(messageToClient));
            }
        }else if (messageFromClient.getType().equals("2")) {
            // 私聊信息
            // {"to":"0-1-2-","msg":"33333","type":2}
            // 私聊内容
            String content = messageFromClient.getMsg();
            int toL = messageFromClient.getTo().length();
            String tos[] = messageFromClient.getTo()
                    .substring(0,toL-1).split("-");
            List<String> lists = Arrays.asList(tos);
            // 给指定的SessionID发送信息
            for (WebSocket webSocket : clients) {
                if (lists.contains(webSocket.session.getId()) &&
                        this.session.getId() != webSocket.session.getId()) {
                    // 发送私聊信息
                    MessageToClient messageToClient = new MessageToClient();
                    messageToClient.setContent(userName,content);
                    messageToClient.setNames(namespaces);
                    webSocket.sendMsg(CommUtils.object2Json(messageToClient));
                }
            }
        }
    }

    @OnClose
    // 关闭连接
    public void onClose() {
        // 将客户端聊天实体移除
        clients.remove(this);
        // 将当前用户以及SessionID保存到用户列表
        namespaces.remove(session.getId());
        System.out.println("有连接下线了"+
                ",用户名为"+userName);
        // 发送给所有在线用户一个下线通知
        MessageToClient messageToClient = new MessageToClient();
        messageToClient.setContent(userName+"下线了!");
        messageToClient.setNames(namespaces);
        // 发送信息
        String jsonStr = CommUtils.object2Json(messageToClient);
        for (WebSocket webSocket : clients) {
            webSocket.sendMsg(jsonStr);
        }
    }

    // 发送信息
    public void sendMsg(String msg) {
        try {
            this.session.getBasicRemote().sendText(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
