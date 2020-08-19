package com.chartroom.dao;

import com.chartroom.entity.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;

import static javax.swing.UIManager.getString;

/**
 * @Author: Mr.Q
 * @Date: 2019-08-10 08:33
 * @Description:关于用户模块的dao层
 */
public class AccountDao extends BaseDao {

    /**
     * 用户登录 select
     * @return
     */
    public User userLogin(String userName,String password) {
        User user = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection();
            String sql = "select * from user where username = ? and password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userName);
            preparedStatement.setString(2, DigestUtils.md5Hex(password));
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                user = getUserInfo(resultSet);
            }
        }catch (Exception e) {
            System.err.println("查询用户信息出错！");
            e.printStackTrace();
        }finally {
            closeResources(resultSet);
            closeResources(connection,preparedStatement);
        }
        return user;
    }

    // 用户注册 insert
    // 通过 User类的对象传入
    public boolean userRegister(User user) {
        String userName = user.getUsername();
        String password = user.getPassword();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        boolean isSuccess = false;
        try {
            connection = getConnection();
            String sql = "INSERT INTO user(username, password)" +
                    " VALUES(?,?) ";
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,userName);
            preparedStatement.setString(2,DigestUtils.md5Hex(password));
            isSuccess = (preparedStatement.executeUpdate() == 1);
        }catch (Exception e) {
            System.err.println("用户注册失败");
            e.printStackTrace();
        }finally {
            closeResources(connection,preparedStatement);
        }
        return isSuccess;
    }


    // 将数据表信息封装到 User类中
    public User getUserInfo(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        return user;
    }
}
