package com.dao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Objects;
import java.util.Properties;

/**
 * @Description 持久层操作mysql
 * @Date 2021/1/10 23:33
 * @Version 1.0
 */
public class BaseDao {
    private static String driver;
    private static String url;
    private static String userName;
    private static String password;
    
    static {
        Properties ppt = new Properties();
        InputStream in = BaseDao.class.getClassLoader().getResourceAsStream("database.properties");
        try {
            ppt.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        driver = ppt.getProperty("driver");
        url = ppt.getProperty("url");
        userName = ppt.getProperty("user");
        password = ppt.getProperty("password");
    }
    
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection conn = null;
        Class.forName(driver);
        conn = DriverManager.getConnection(url, userName, password);
        return conn;
    }
    
    public static ResultSet executeQuery(Connection conn, PreparedStatement ps, String sql, Object[] params) throws SQLException {
        ps = conn.prepareStatement(sql);
        if (Objects.nonNull(params)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
        return ps.executeQuery();
    }
    
    public static boolean executeUpdate(Connection conn, PreparedStatement ps, String sql, Object[] params) throws SQLException {
        ps = conn.prepareStatement(sql);
        if (Objects.nonNull(params)) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
        int result = ps.executeUpdate();
        return result > 0;
    }
    
    public static void closeResource(Connection conn, PreparedStatement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
                ps = null;
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }
}
