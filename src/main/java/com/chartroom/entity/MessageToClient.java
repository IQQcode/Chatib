package com.chartroom.entity;


import lombok.Data;

import java.util.Map;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-12 10:55
 * @Description:后端发送给前端的信息实体
 */
@Data
public class MessageToClient {
    // 聊天内容
    private String content;

    // 服务端要登录的所有用户列表
    private Map<String,String> names;

    public void setContent(String msg) {
        this.content = msg;
    }
    public void setContent(String userName,String msg) {
        this.content = userName + "说:" + msg;
    }
}
