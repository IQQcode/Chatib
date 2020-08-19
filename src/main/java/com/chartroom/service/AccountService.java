package com.chartroom.service;


import com.chartroom.dao.AccountDao;
import com.chartroom.entity.User;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-10 10:48
 * @Description:业务层 数据库的校验和插入
 */
public class AccountService {
    private AccountDao accountDao = new AccountDao();

    /**
     * 用户登录 业务层
     * @param username
     * @param password
     * @return
     */
    public boolean userLogin(String username,String password) {
        User user = accountDao.userLogin(username, password);
        if(user == null) {
            return false;
        }
        return true;
    }

    /**
     * 用户注册
     * @param username
     * @param password
     * @return
     */
    public boolean userRegister(String username,String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return accountDao.userRegister(user);
    }
}
