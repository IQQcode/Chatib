package com.chartroom.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-01 07:56
 * @Description:封装基础的工具方法(如加载配置文件，Json序列化)
 */

public class CommUtils {

    private static final Gson gson = new GsonBuilder().create();
    private CommUtils(){ }

    /**
     * 根据指定的文件名加载配置文件
     * @param fileName 配置文件名
     * @return
     */
    public static Properties loadProperties(String fileName) {
        Properties properties = new Properties();
        // 获取当前配置文件夹下的文件输入流
        InputStream in = CommUtils.class.getClassLoader()
                .getResourceAsStream(fileName);
        // 加载配置文件中的所有内容
        try {
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String object2Json(Object obj) {
        return gson.toJson(obj);
    }

    public static Object json2Object(String jsonStr,Class objClass) {
        return gson.fromJson(jsonStr,objClass);
    }

    // 判断表单提交的 username或 password是否为空
    public static boolean strIsNull(String str) {
        return str ==null || str.equals("");
    }
}

