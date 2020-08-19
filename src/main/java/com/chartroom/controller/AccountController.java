package com.chartroom.controller;


import com.chartroom.service.AccountService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-10 11:17
 * @Description: 注册
 */
@WebServlet(urlPatterns = {"/doRegister"}) //注解来配置 web.xml
public class AccountController extends HttpServlet {
    private AccountService accountService = new AccountService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 获取用户输入的 username,password(在表单中提交的值)
        String userName = req.getParameter("username");
        String password = req.getParameter("password");
        resp.setContentType("text/html;charset=utf8");
        PrintWriter writer = resp.getWriter(); //获取页面输出流

        if(accountService.userRegister(userName, password)) {
            // 用户注册成功
            //提示返回登录页面
            writer.println("<script>\n" +
                    "    alert(\"注册成功\");\n" +
                    "    window.location.href =\"/index.html\";\n" +
                    "</script>");

        }else {
            // 提示注册失败,保留原页面(还停留在注册页面)
            writer.println("<script>\n" +
                    "    alert(\"注册失败！！！\");\n" +
                    "    window.location.href =\"/registration.html\";\n" +
                    "</script>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
