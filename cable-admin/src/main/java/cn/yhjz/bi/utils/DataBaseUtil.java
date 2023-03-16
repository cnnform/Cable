package cn.yhjz.bi.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DB操作
 *
 * @author ldl
 */
public class DataBaseUtil {

    private final static Logger loggin = LoggerFactory.getLogger(DataBaseUtil.class);

    /**
     * 数据库连接
     *
     * @param driver > 驱动
     * @param url > URL
     * @param username > 用户名
     * @param password > 密码
     */
    public static Connection connectDb(String driver, String url, String username, String password) {
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            loggin.error("DriverClass or Connect Failing! errorMsg: " + e.getMessage());
            return null;
        }
        return connection;
    }

}
