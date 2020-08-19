package com.chartroom.entity;


import lombok.Data;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-12 10:50
 * @Description:前端发送给后端的信息实体类
 *
 * 群聊:{"msg":"666","type":1}
 * 私聊:{"to":"0-","msg":"88888","type":2}
 */
@Data
public class MessageFromClient {
    // 聊天信息
    private String msg;
    // 聊天类别: 1 表示群聊   2 表示私聊
    private String type;
    //  私聊对象的 SessionID, 根据SessionID到服务端找对应的 WebSocket
    private String to;
}
