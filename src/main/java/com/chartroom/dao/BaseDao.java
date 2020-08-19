package com.chartroom.dao;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.chartroom.utils.CommUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import	java.sql.ResultSet;
import java.util.Properties;


/**
 * @Author: Mr.Q
 * @Date: 2019-08-10 07:49
 * @Description:封装基础操作,数据源,获取连接,关闭资源
 */
public class BaseDao {
    private static DataSource dataSource;

    // 1.加载数据源
    static {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        try {
            dataSource = (DataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            System.err.println("数据源加载失败！");
        }
    }

    // 2.获取数据库连接
    protected Connection getConnection() {
        try {
            return dataSource.getConnection();
        }catch (Exception e) {
            System.err.println("获取连接失败！");
        }
        return null;
    }

    // 3.关闭资源 Statement,Result,Connection
    protected void closeResources(ResultSet resultSet) {
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    protected void closeResources(Connection connection, Statement statement) {
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if(statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void closeConnection(Connection connection,
                                   Statement statement,
                                   ResultSet resultSet) {
        closeResources(connection, statement);
        if(resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
