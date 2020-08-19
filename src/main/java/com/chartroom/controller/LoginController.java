package com.chartroom.controller;
import	java.io.PrintWriter;


import com.chartroom.config.FreeMarkerListener;
import com.chartroom.service.AccountService;
import com.chartroom.utils.CommUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-12 08:56
 * @Description:
 */
@WebServlet(urlPatterns = "/login")
public class LoginController extends HttpServlet {
    private AccountService accountService = new AccountService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userName = req.getParameter("username"); // 要和表单中的参数一致
        String password = req.getParameter("password");
        resp.setContentType("text/html;charset=utf8");
        PrintWriter out = resp.getWriter();
        if(CommUtils.strIsNull(userName) || CommUtils.strIsNull(password)) {
            // 登陆失败后返回登录页面
            out.println("<script>\n" +
                    "    alert(\"用户名或密码为空！\");\n" +
                    "    window.location.href = \"/index.html\";\n" +
                    "</script>");
        }
        // 到数据库中对比 username 和 password是否一致
        if(accountService.userLogin(userName,password)) {
            // 登陆成功,跳转到聊天页面
            Template template = getTemplate(req,"/chat.ftl");
            Map<String,String> map = new HashMap<>();
            map.put("username", userName);
            try {
                //通过 map集合传入到前端
                template.process(map, out);
            } catch (TemplateException e) {
                e.printStackTrace();
            }
        }else {
            // 登陆失败，跳转到登录页面
            out.println("<script>\n" +
                    "    alert(\"用户名或密码不正确！\");\n" +
                    "    window.location.href = \"/index.html\";\n" +
                    "</script>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp); // 将 doPost的请求转到 doGet

    }

    // 加载 chat.ftl
    private Template getTemplate(HttpServletRequest req,String fileName) {
        // 获取设置的配置
        Configuration cfg = (Configuration)
                req.getServletContext().getAttribute(FreeMarkerListener.TEMPLATE_KEY);

        try {
            return cfg.getTemplate(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
